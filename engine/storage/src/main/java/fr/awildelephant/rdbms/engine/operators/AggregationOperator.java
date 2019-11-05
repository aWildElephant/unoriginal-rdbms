package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.sort.DateColumnComparator;
import fr.awildelephant.rdbms.engine.operators.sort.DecimalColumnComparator;
import fr.awildelephant.rdbms.engine.operators.sort.IntegerColumnComparator;
import fr.awildelephant.rdbms.engine.operators.sort.RecordComparator;
import fr.awildelephant.rdbms.engine.operators.sort.TextColumnComparator;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
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
        if (aggregate instanceof AvgAggregate) {
            return computeAvgAggregate(((AvgAggregate) aggregate).input(), inputTable);
        } else if (aggregate instanceof CountStarAggregate) {
            return computeCountStarAggregation(inputTable);
        } else if (aggregate instanceof MinAggregate) {
            return computeMinAggregate(((MinAggregate) aggregate).input(), inputTable);
        } else if (aggregate instanceof SumAggregate) {
            final ColumnReference inputColumn = ((SumAggregate) aggregate).input();
            if (inputTable.schema().column(inputColumn).domain() == INTEGER) {
                return computeIntegerSumAggregate(inputColumn, inputTable);
            } else {
                return computeDecimalSumAggregate(inputColumn, inputTable);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private DomainValue computeAvgAggregate(ColumnReference inputName, Table inputTable) {
        final int inputIndex = outputSchema.indexOf(inputName);
        BigDecimal accumulator = null;
        int numberOfNotNullValues = 0;

        for (Record record : inputTable) {
            final DomainValue value = record.get(inputIndex);

            if (!value.isNull()) {
                numberOfNotNullValues++;

                final BigDecimal newValue = value.getBigDecimal();
                if (accumulator == null) {
                    accumulator = newValue;
                } else {
                    accumulator = accumulator.add(newValue);
                }
            }
        }

        if (accumulator == null) {
            return nullValue();
        } else {
            return decimalValue(accumulator.divide(BigDecimal.valueOf(numberOfNotNullValues), MathContext.DECIMAL64));
        }
    }

    private DomainValue computeMinAggregate(ColumnReference inputName, Table inputTable) {
        final Column column = inputTable.schema().column(inputName);
        final int columnIndex = column.index();

        final RecordComparator comparator;
        // TODO: create a factory method for this
        switch (column.domain()) {
            case DATE:
                comparator = new DateColumnComparator(columnIndex);
                break;
            case DECIMAL:
                comparator = new DecimalColumnComparator(columnIndex);
                break;
            case INTEGER:
                comparator = new IntegerColumnComparator(columnIndex);
                break;
            case TEXT:
                comparator = new TextColumnComparator(columnIndex);
                break;
            default:
                throw new UnsupportedOperationException(); // TODO 8)
        }

        Record minRecord = null;

        for (Record record : inputTable) {
            if (minRecord == null || comparator.compare(record, minRecord) < 0) {
                minRecord = record;
            }
        }

        if (minRecord == null) {
            return nullValue();
        } else {
            return minRecord.get(columnIndex);
        }
    }

    private DomainValue computeIntegerSumAggregate(ColumnReference inputName, Table inputTable) {
        final int inputIndex = outputSchema.indexOf(inputName);
        Integer accumulator = null;

        for (Record record : inputTable) {
            final DomainValue value = record.get(inputIndex);

            if (!value.isNull()) {
                final int newValue = value.getInt();
                if (accumulator == null) {
                    accumulator = newValue;
                } else {
                    accumulator = accumulator + newValue;
                }
            }
        }

        if (accumulator == null) {
            return nullValue();
        } else {
            return integerValue(accumulator);
        }
    }

    private DomainValue computeDecimalSumAggregate(ColumnReference inputName, Table inputTable) {
        final int inputIndex = outputSchema.indexOf(inputName);
        BigDecimal accumulator = null;

        for (Record record : inputTable) {
            final DomainValue value = record.get(inputIndex);

            if (!value.isNull()) {
                final BigDecimal newValue = value.getBigDecimal();
                if (accumulator == null) {
                    accumulator = newValue;
                } else {
                    accumulator = accumulator.add(newValue);
                }
            }
        }

        if (accumulator == null) {
            return nullValue();
        } else {
            return decimalValue(accumulator);
        }
    }

    private DomainValue computeCountStarAggregation(Table inputTable) {
        return integerValue(inputTable.numberOfTuples());
    }
}
