package fr.awildelephant.rdbms.engine.constraint;

import fr.awildelephant.rdbms.engine.data.record.Record;

public interface ConstraintChecker {

    void check(Record record);
}
