package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.JoinValues;
import fr.awildelephant.rdbms.evaluator.Formula;

public final class NestedLoopSemiJoinMatcher implements SemiJoinMatcher {

    private final Table rightTable;
    private final Formula joinSpecification;

    public NestedLoopSemiJoinMatcher(Table rightTable, Formula joinSpecification) {
        this.rightTable = rightTable;
        this.joinSpecification = joinSpecification;
    }

    @Override
    public boolean match(Record leftRecord) {
        final JoinValues values = new JoinValues(leftRecord);

        for (Record rightRecord : rightTable) {
            values.setRightRecord(rightRecord);
            if (joinSpecification.evaluate(values).getBool()) {
                return true;
            }
        }

        return false;
    }
}
