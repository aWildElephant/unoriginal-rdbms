package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.JoinValues;
import fr.awildelephant.rdbms.evaluator.Formula;

import java.util.ArrayList;
import java.util.List;

public class NestedLoopSemiJoinMatcher implements SemiJoinMatcher {

    private final Table rightTable;
    private final Formula joinSpecification;

    public NestedLoopSemiJoinMatcher(Table rightTable, Formula joinSpecification) {
        this.rightTable = rightTable;
        this.joinSpecification = joinSpecification;
    }

    @Override
    public boolean match(Record leftRecord) {
        final JoinValues values = new JoinValues(leftRecord.size());

        final List<Record> matchingRightRecords = new ArrayList<>();
        for (Record rightRecord : rightTable) {
            values.setRecords(leftRecord, rightRecord);
            if (joinSpecification.evaluate(values).getBool()) {
                return true;
            }
        }

        return false;
    }
}
