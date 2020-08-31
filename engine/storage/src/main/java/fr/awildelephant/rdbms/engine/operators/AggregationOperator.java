package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.aggregation.*;
import fr.awildelephant.rdbms.plan.aggregation.*;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.*;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
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
        if (inputTable.isEmpty()) {
            return computeAggregationOnEmptyTable();
        }

        final Map<Record, List<Aggregator>> accumulators = new HashMap<>();

        // TODO: have an operator/branch for an aggregation without breakdowns?
        final Schema inputSchema = inputTable.schema();
        final int[] columnIndexes = new int[breakdowns.size()];
        for (int i = 0; i < breakdowns.size(); i++) {
            columnIndexes[i] = inputSchema.column(breakdowns.get(i)).index();
        }

        final int numberOfAggregates = aggregates.size();
        final OptionalInt[] inputColumnIndexByAggregate = inputColumnIndexByAggregate(aggregates, inputSchema);

        for (Record record : inputTable) {
            final Record group = project(record, columnIndexes);
            final List<Aggregator> aggregators = accumulators
                    .computeIfAbsent(group, unused -> buildAccumulatorList(inputSchema));

            for (int aggregateIndex = 0; aggregateIndex < numberOfAggregates; aggregateIndex++) {
                final OptionalInt inputColumnIndex = inputColumnIndexByAggregate[aggregateIndex];
                final Aggregator aggregator = aggregators.get(aggregateIndex);

                inputColumnIndex.ifPresentOrElse(
                        value -> aggregator.accumulate(record.get(value)),
                        () -> aggregator.accumulate(nullValue())
                );
            }
        }

        return buildOutputTable(accumulators);
    }

    private OptionalInt[] inputColumnIndexByAggregate(List<Aggregate> aggregates, Schema inputSchema) {
        final OptionalInt[] indexes = new OptionalInt[aggregates.size()];

        for (int aggregateIndex = 0; aggregateIndex < aggregates.size(); aggregateIndex++) {
            final Aggregate aggregate = aggregates.get(aggregateIndex);

            final Optional<ColumnReference> aggregateInputColumn = aggregate.inputColumn();

            indexes[aggregateIndex] = aggregateInputColumn
                    .map(columnReference -> OptionalInt.of(inputSchema.column(columnReference).index()))
                    .orElseGet(OptionalInt::empty);
        }

        return indexes;
    }

    private List<Aggregator> buildAccumulatorList(Schema inputSchema) {
        final List<Aggregator> accumulatorList = new ArrayList<>(aggregates.size());

        for (Aggregate aggregate : aggregates) {
            accumulatorList.add(aggregator(aggregate, inputSchema));
        }

        return accumulatorList;
    }

    private Record project(Record record, int[] columnIndexes) {
        final DomainValue[] values = new DomainValue[columnIndexes.length];

        int i = 0;
        for (int columnIndex : columnIndexes) {
            values[i] = record.get(columnIndex);

            i++;
        }

        return new Record(values);
    }

    private Table buildOutputTable(Map<Record, List<Aggregator>> accumulators) {
        final Table outputTable = simpleTable(outputSchema, accumulators.size());
        final List<Column> outputColumns = outputTable.columns();

        final int numberOfBreakdownColumns = breakdowns.size();
        final int numberOfAggregateColumns = aggregates.size();

        accumulators.forEach((breakdownValues, aggregateValues) -> {
            for (int i = 0; i < numberOfBreakdownColumns; i++) {
                outputColumns.get(i).add(breakdownValues.get(i));
            }

            for (int i = 0; i < numberOfAggregateColumns; i++) {
                outputColumns.get(numberOfBreakdownColumns + i).add(aggregateValues.get(i).aggregate());
            }
        });

        return outputTable;
    }

    private Table computeAggregationOnEmptyTable() {
        final int numberOfOutputColumns = outputSchema.numberOfAttributes();
        final int numberOfAggregates = aggregates.size();

        final Table outputTable = simpleTable(outputSchema, 1);
        final List<Column> outputColumns = outputTable.columns();

        final int firstAggregateIndex = numberOfOutputColumns - numberOfAggregates;
        for (int i = 0; i < firstAggregateIndex; i++) {
            outputColumns.get(i).add(nullValue());
        }

        for (int i = 0; i < numberOfAggregates; i++) {
            if (aggregates.get(i).outputIsNullable()) {
                outputColumns.get(i).add(nullValue());
            } else {
                outputColumns.get(i).add(integerValue(0));
            }
        }

        return outputTable;
    }

    private Aggregator aggregator(Aggregate aggregate, Schema schema) {
        if (aggregate instanceof AnyAggregate) {
            return new AnyAggregator();
        } else if (aggregate instanceof AvgAggregate) {
            return new AvgAggregator();
        } else if (aggregate instanceof CountAggregate) {
            if (((CountAggregate) aggregate).distinct()) {
                return new CountDistinctAggregator();
            } else {
                return new CountAggregator();
            }
        } else if (aggregate instanceof CountStarAggregate) {
          return new CountStarAggregator();
        } else if (aggregate instanceof MaxAggregate) {
            final ColumnMetadata inputColumn = schema.column(aggregate.inputColumn().orElseThrow());

            return new MaxAggregator(comparator(inputColumn));
        } else if (aggregate instanceof MinAggregate) {
            final ColumnMetadata inputColumn = schema.column(aggregate.inputColumn().orElseThrow());

            return new MinAggregator(comparator(inputColumn));
        } else if (aggregate instanceof SumAggregate) {
            final ColumnMetadata inputColumn = schema.column(aggregate.inputColumn().orElseThrow());

            if (inputColumn.domain() == INTEGER) {
                return new IntegerSumAggregator();
            } else {
                return new DecimalSumAggregator();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    private Comparator<DomainValue> comparator(ColumnMetadata column) {
        switch (column.domain()) {
            case DATE:
                return Comparator.comparing(DomainValue::getLocalDate);
            case DECIMAL:
                return Comparator.comparing(DomainValue::getBigDecimal);
            case INTEGER:
                return Comparator.comparingInt(DomainValue::getInt);
            case TEXT:
                return Comparator.comparing(DomainValue::getString);
            default:
                throw new UnsupportedOperationException(); // TODO
        }
    }
}
