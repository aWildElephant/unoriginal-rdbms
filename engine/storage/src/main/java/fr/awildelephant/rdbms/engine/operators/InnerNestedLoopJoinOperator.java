package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.JoinValues;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static fr.awildelephant.rdbms.engine.operators.JoinUtils.joinRecords;

public class InnerNestedLoopJoinOperator implements JoinOperator {

    private final Formula joinSpecification;
    private final Schema leftInputSchema;
    private final Schema rightInputSchema;
    private final Schema outputSchema;

    public InnerNestedLoopJoinOperator(Formula joinSpecification, Schema leftInputSchema, Schema rightInputSchema, Schema outputSchema) {
        this.joinSpecification = joinSpecification;
        this.leftInputSchema = leftInputSchema;
        this.rightInputSchema = rightInputSchema;
        this.outputSchema = outputSchema;

    }

    @Override
    public Table compute(Table left, Table right) {
        final Table outputTable = simpleTable(outputSchema, left.numberOfTuples() * right.numberOfTuples());

        final JoinValues values = new JoinValues(leftInputSchema, rightInputSchema);

        for (Record leftrecord : left) {
            for (Record rightRecord : right) {
                values.setRecords(leftrecord, rightRecord);
                if (joinSpecification.evaluate(values).getBool()) {
                    outputTable.add(joinRecords(leftrecord, rightRecord));
                }
            }
        }

        return outputTable;
    }

}
