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
    private final Schema outputSchema;

    public InnerNestedLoopJoinOperator(Formula joinSpecification, Schema outputSchema) {
        this.joinSpecification = joinSpecification;
        this.outputSchema = outputSchema;

    }

    @Override
    public Table compute(Table left, Table right) {
        final Table outputTable = simpleTable(outputSchema, left.numberOfTuples() * right.numberOfTuples());

        final JoinValues values = new JoinValues(left.schema().numberOfAttributes());

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
