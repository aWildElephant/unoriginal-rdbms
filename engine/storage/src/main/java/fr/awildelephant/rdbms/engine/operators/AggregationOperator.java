package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.aggregation.Aggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.AnyAggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.AvgAggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.CountAggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.CountDistinctAggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.DecimalSumAggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.IntegerSumAggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.MaxAggregator;
import fr.awildelephant.rdbms.engine.operators.aggregation.MinAggregator;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AnyAggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MaxAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Comparator;
import java.util.List;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class AggregationOperator implements Operator<Table, Table> {

    private final List<Aggregate> aggregates;
    private final Schema outputSchema;

    public AggregationOperator(List<Aggregate> aggregates, Schema outputSchema) {
        this.aggregates = aggregates;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(Table inputTable) {
        final int numberOfOutputColumns = outputSchema.numberOfAttributes();
        final DomainValue[] outputValues = new DomainValue[numberOfOutputColumns];
        final int numberOfTuples = inputTable.numberOfTuples();

        if (numberOfTuples == 0) {
            final int numberOfAggregates = aggregates.size();
            for (int i = 0; i < numberOfOutputColumns - numberOfAggregates; i++) {
                outputValues[i] = nullValue();
            }

            final int firstAggregateIndex = numberOfOutputColumns - numberOfAggregates;
            for (int i = 0; i < numberOfAggregates; i++) {
                outputValues[firstAggregateIndex + i] = (aggregates.get(i)
                                                                   .outputIsNullable()) ? nullValue() : integerValue(0);
            }
        } else {
            final Record firstRecord = inputTable.iterator().next();
            final int numberOfAggregates = aggregates.size();
            for (int i = 0; i < numberOfOutputColumns - numberOfAggregates; i++) {
                outputValues[i] = firstRecord.get(i);
            }

            final int firstAggregateIndex = numberOfOutputColumns - numberOfAggregates;
            for (int i = 0; i < numberOfAggregates; i++) {
                outputValues[firstAggregateIndex + i] = computeAggregation(aggregates.get(i), inputTable);
            }
        }

        final Table outputTable = simpleTable(outputSchema);

        outputTable.add(new Record(outputValues));

        return outputTable;
    }

    private DomainValue computeAggregation(Aggregate aggregate, Table inputTable) {
        if (aggregate instanceof CountStarAggregate) {
            return integerValue(inputTable.numberOfTuples());
        }

        final Schema inputSchema = inputTable.schema();

        final Aggregator aggregator = aggregator(aggregate, inputSchema);

        final int inputColumnIndex = inputSchema.column(aggregate.inputColumn().orElseThrow()).index();

        for (Record record : inputTable) {
            final boolean done = aggregator.accumulate(record.get(inputColumnIndex));

            if (done) {
                break;
            }
        }

        return aggregator.aggregate();
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
        } else if (aggregate instanceof MaxAggregate) {
            final Column inputColumn = schema.column(aggregate.inputColumn().orElseThrow());

            return new MaxAggregator(comparator(inputColumn));
        } else if (aggregate instanceof MinAggregate) {
            final Column inputColumn = schema.column(aggregate.inputColumn().orElseThrow());

            return new MinAggregator(comparator(inputColumn));
        } else if (aggregate instanceof SumAggregate) {
            final Column inputColumn = schema.column(aggregate.inputColumn().orElseThrow());

            if (inputColumn.domain() == INTEGER) {
                return new IntegerSumAggregator();
            } else {
                return new DecimalSumAggregator();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    private Comparator<DomainValue> comparator(Column column) {
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
