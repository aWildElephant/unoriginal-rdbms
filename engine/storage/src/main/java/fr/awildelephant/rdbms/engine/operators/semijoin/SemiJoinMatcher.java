package fr.awildelephant.rdbms.engine.operators.semijoin;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

public interface SemiJoinMatcher {

    ThreeValuedLogic match(Record leftRecord);
}
