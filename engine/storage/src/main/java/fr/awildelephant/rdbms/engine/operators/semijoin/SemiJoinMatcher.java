package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.record.Record;

public interface SemiJoinMatcher {

    // FIXME: should be able to return 'unknown'
    boolean match(Record leftRecord);
}
