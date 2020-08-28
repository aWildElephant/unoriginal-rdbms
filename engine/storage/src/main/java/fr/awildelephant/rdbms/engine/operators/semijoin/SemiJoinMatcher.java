package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.record.Record;

public interface SemiJoinMatcher {

    boolean match(Record leftRecord);
}
