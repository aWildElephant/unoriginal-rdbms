package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.Tuple;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.WriteableTable;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class CartesianProductOperator implements Operator {

    private final String leftInputKey;
    private final String rightInputKey;
    private final Schema outputSchema;

    public CartesianProductOperator(String leftInputKey, String rightInputKey, Schema outputSchema) {
        this.leftInputKey = leftInputKey;
        this.rightInputKey = rightInputKey;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table leftInput = storage.get(leftInputKey);
        final Table rightInput = storage.get(rightInputKey);

        final int outputSize = leftInput.numberOfTuples() * rightInput.numberOfTuples();
        final WriteableTable outputTable = simpleTable(outputSchema, outputSize);

        for (Record left : leftInput) {
            for (Record right : rightInput) {
                outputTable.add(joinRecords(left, right));
            }
        }

        return outputTable;
    }

    private Tuple joinRecords(Record leftRecord, Record rightRecord) {
        final DomainValue[] values = new DomainValue[outputSchema.numberOfAttributes()];

        final int numberOfColumnsFromLeftTable = leftRecord.size();

        for (int i = 0; i < numberOfColumnsFromLeftTable; i++) {
            values[i] = leftRecord.get(i);
        }

        for (int i = 0; i < rightRecord.size(); i++) {
            values[numberOfColumnsFromLeftTable + i] = rightRecord.get(i);
        }


        return new Tuple(values);
    }
}
