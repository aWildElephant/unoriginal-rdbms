package fr.awildelephant.rdbms.execution.operator.semijoin;

import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

public interface SemiJoinMatcher {

    ThreeValuedLogic match(Record leftRecord);
}
