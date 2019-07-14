package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.JoinValues;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public class InnerJoinOperator {

    private final Formula joinSpecification;
    private final Schema leftInputSchema;
    private final Schema rightInputSchema;
    private final Schema outputSchema;

    public InnerJoinOperator(Formula joinSpecification, Schema leftInputSchema, Schema rightInputSchema, Schema outputSchema) {
        this.joinSpecification = joinSpecification;
        this.leftInputSchema = leftInputSchema;
        this.rightInputSchema = rightInputSchema;
        this.outputSchema = outputSchema;

    }

    public Table compute(Table leftInput, Table rightInput) {
        final Table outputTable = simpleTable(outputSchema, leftInput.numberOfTuples() * rightInput.numberOfTuples());

        final JoinValues values = new JoinValues(leftInputSchema, rightInputSchema);

        for (Record left : leftInput) {
            for (Record right : rightInput) {
                values.setRecords(left, right);
                if (joinSpecification.evaluate(values).getBool()) {
                    outputTable.add(joinRecords(left, right));
                }
            }
        }

        return outputTable;
    }

    private Record joinRecords(Record leftRecord, Record rightRecord) {
        final DomainValue[] values = new DomainValue[outputSchema.numberOfAttributes()];

        final int numberOfColumnsFromLeftTable = leftRecord.size();

        for (int i = 0; i < numberOfColumnsFromLeftTable; i++) {
            values[i] = leftRecord.get(i);
        }

        for (int i = 0; i < rightRecord.size(); i++) {
            values[numberOfColumnsFromLeftTable + i] = rightRecord.get(i);
        }


        return new Record(values);
    }
}
