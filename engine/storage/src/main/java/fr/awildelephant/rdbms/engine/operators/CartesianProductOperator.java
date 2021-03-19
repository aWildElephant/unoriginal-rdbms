package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.Tuple;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class CartesianProductOperator {

    private final Schema outputSchema;

    public CartesianProductOperator(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

    public Table compute(Table leftInput, Table rightInput) {
        final Table outputTable = simpleTable(outputSchema, leftInput.numberOfTuples() * rightInput.numberOfTuples());

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
