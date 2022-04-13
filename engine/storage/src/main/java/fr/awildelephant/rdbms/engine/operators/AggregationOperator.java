package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.Tuple;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.accumulator.Accumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.AnyAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.AvgAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.CountAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.CountDistinctAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.CountStarAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.DecimalSumAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.IntegerSumAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.MaxAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.MinAccumulator;
import fr.awildelephant.rdbms.engine.operators.accumulator.wrapper.AccumulatorWithInputWrapper;
import fr.awildelephant.rdbms.engine.operators.accumulator.wrapper.AccumulatorWithNoInputWrapper;
import fr.awildelephant.rdbms.engine.operators.accumulator.wrapper.AccumulatorWrapper;
import fr.awildelephant.rdbms.engine.operators.hashing.HashingHelper;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AnyAggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MaxAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class AggregationOperator implements Operator<Table, Table> {

    private final List<Aggregate> aggregates;
    private final List<ColumnReference> breakdowns;
    private final Schema outputSchema;

    public AggregationOperator(List<Aggregate> aggregates,
                               List<ColumnReference> breakdowns,
                               Schema outputSchema) {
        this.aggregates = aggregates;
        this.breakdowns = breakdowns;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
        final Map<Record, List<Record>> hash = hashInputTable(inputTable, breakdowns);

        final Supplier<List<AccumulatorWrapper>> accumulatorListSupplier = accumulatorListSupplier(inputTable.schema());

        final Table outputTable = simpleTable(outputSchema);
        final List<Column> outputColumns = outputTable.columns();
        hash.forEach((group, rows) -> {
            final List<AccumulatorWrapper> accumulatorList = accumulatorListSupplier.get();

            rows.forEach(row -> accumulatorList.forEach(accumulator -> accumulator.accumulate(row)));

            // Add computed row to output table
            final int numberOfBreakdowns = group.size();
            for (int breakdownIndex = 0; breakdownIndex < numberOfBreakdowns; breakdownIndex++) {
                outputColumns.get(breakdownIndex).add(group.get(breakdownIndex));
            }
            for (int aggregateIndex = 0; aggregateIndex < accumulatorList.size(); aggregateIndex++) {
                outputColumns.get(numberOfBreakdowns + aggregateIndex).add(accumulatorList.get(aggregateIndex).result());
            }
        });

        return outputTable;
    }

    private Map<Record, List<Record>> hashInputTable(Table inputTable, List<ColumnReference> breakdowns) {
        if (breakdowns.isEmpty()) {
            if (inputTable.isEmpty()) {
                return Map.of(Tuple.EMPTY_TUPLE, List.of(nullRecord(inputTable.schema().numberOfAttributes())));
            } else {
                return Map.of(Tuple.EMPTY_TUPLE, toList(inputTable));
            }
        }

        final int[] mapping = buildMapping(inputTable.schema(), breakdowns);

        return HashingHelper.hash(inputTable, mapping);
    }

    private Record nullRecord(int numberOfAttributes) {
        final DomainValue[] values = new DomainValue[numberOfAttributes];
        for (int i = 0; i < numberOfAttributes; i++) {
            values[i] = nullValue();
        }
        return new Tuple(values);
    }

    // FIXME: stop building this list
    private List<Record> toList(Table table) {
        final List<Record> list = new ArrayList<>(table.numberOfTuples());
        for (Record element : table) {
            list.add(element.materialize());
        }
        return list;
    }

    public Supplier<List<AccumulatorWrapper>> accumulatorListSupplier(Schema inputSchema) {
        final int[] aggregateMapping = buildMapping(inputSchema, aggregates.stream()
                .map(aggregate -> aggregate.inputColumn().orElse(null))
                .collect(Collectors.toList()));

        final List<Supplier<Accumulator>> accumulatorSupplierList = aggregates.stream()
                .map(aggregate -> (Supplier<Accumulator>) () -> accumulator(aggregate, inputSchema))
                .toList();

        return () -> {
            final List<AccumulatorWrapper> accumulatorList = new ArrayList<>(aggregates.size());
            for (int aggregateIndex = 0; aggregateIndex < accumulatorSupplierList.size(); aggregateIndex++) {
                Supplier<Accumulator> accumulatorSupplier = accumulatorSupplierList.get(aggregateIndex);
                final int inputColumnIndex = aggregateMapping[aggregateIndex];
                final AccumulatorWrapper wrapper;
                if (inputColumnIndex < 0) {
                    wrapper = new AccumulatorWithNoInputWrapper(accumulatorSupplier.get());
                } else {
                    wrapper = new AccumulatorWithInputWrapper(accumulatorSupplier.get(), inputColumnIndex);
                }
                accumulatorList.add(wrapper);
            }
            return accumulatorList;
        };
    }

    private int[] buildMapping(Schema schema, List<ColumnReference> columns) {
        final int[] mapping = new int[columns.size()];
        int mappingIndex = 0;
        for (ColumnReference column : columns) {
            mapping[mappingIndex++] = column != null ? schema.column(column).index() : -1;
        }
        return mapping;
    }

    private Accumulator accumulator(Aggregate aggregate, Schema schema) {
        if (aggregate instanceof AnyAggregate) {
            return new AnyAccumulator();
        } else if (aggregate instanceof AvgAggregate) {
            return new AvgAccumulator();
        } else if (aggregate instanceof CountAggregate countAggregate) {
            if (countAggregate.distinct()) {
                return new CountDistinctAccumulator();
            } else {
                return new CountAccumulator();
            }
        } else if (aggregate instanceof CountStarAggregate) {
            return new CountStarAccumulator();
        } else if (aggregate instanceof MaxAggregate) {
            final ColumnMetadata inputColumn = schema.column(aggregate.inputColumn().orElseThrow()).metadata();

            return new MaxAccumulator(comparator(inputColumn));
        } else if (aggregate instanceof MinAggregate) {
            final ColumnMetadata inputColumn = schema.column(aggregate.inputColumn().orElseThrow()).metadata();

            return new MinAccumulator(comparator(inputColumn));
        } else if (aggregate instanceof SumAggregate) {
            final ColumnMetadata inputColumn = schema.column(aggregate.inputColumn().orElseThrow()).metadata();

            if (inputColumn.domain() == INTEGER) {
                return new IntegerSumAccumulator();
            } else {
                return new DecimalSumAccumulator();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    private Comparator<DomainValue> comparator(ColumnMetadata column) {
        return switch (column.domain()) {
            case DATE -> Comparator.comparing(DomainValue::getLocalDate);
            case DECIMAL -> Comparator.comparing(DomainValue::getBigDecimal);
            case INTEGER -> Comparator.comparingInt(DomainValue::getInt);
            case TEXT -> Comparator.comparing(DomainValue::getString);
            default -> throw new UnsupportedOperationException(); // TODO
        };
    }
}
