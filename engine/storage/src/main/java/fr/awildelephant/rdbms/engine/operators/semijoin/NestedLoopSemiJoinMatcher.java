package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.values.JoinValues;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public final class NestedLoopSemiJoinMatcher implements SemiJoinMatcher {

    private final Table rightTable;
    private final Formula joinSpecification;

    public NestedLoopSemiJoinMatcher(Table rightTable, Formula joinSpecification) {
        this.rightTable = rightTable;
        this.joinSpecification = joinSpecification;
    }

    @Override
    public ThreeValuedLogic match(Record leftRecord) {
        final JoinValues values = new JoinValues(leftRecord);

        ThreeValuedLogic notFoundValue = FALSE;

        for (Record rightRecord : rightTable) {
            values.setRightRecord(rightRecord);
            final DomainValue result = joinSpecification.evaluate(values);
            if (result.isNull()) {
                notFoundValue = UNKNOWN;
            } else if (result.getBool()) {
                return TRUE;
            }
        }

        return notFoundValue;
    }
}
