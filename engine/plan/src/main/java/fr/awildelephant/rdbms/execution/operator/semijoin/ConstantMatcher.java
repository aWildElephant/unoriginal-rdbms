package fr.awildelephant.rdbms.execution.operator.semijoin;

import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

public final class ConstantMatcher implements SemiJoinMatcher {

    private final ThreeValuedLogic value;

    public ConstantMatcher(ThreeValuedLogic value) {
        this.value = value;
    }

    @Override
    public ThreeValuedLogic match(Record leftRecord) {
        return value;
    }
}
