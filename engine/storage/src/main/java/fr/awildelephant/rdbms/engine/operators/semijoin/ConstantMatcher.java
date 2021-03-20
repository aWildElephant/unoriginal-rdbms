package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.record.Record;

public final class ConstantMatcher implements SemiJoinMatcher {

    private final boolean value;

    public ConstantMatcher(boolean value) {
        this.value = value;
    }

    @Override
    public boolean match(Record leftRecord) {
        return value;
    }
}
