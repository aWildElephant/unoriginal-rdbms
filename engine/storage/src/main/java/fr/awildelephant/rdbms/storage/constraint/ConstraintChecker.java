package fr.awildelephant.rdbms.storage.constraint;

import fr.awildelephant.rdbms.storage.data.record.Record;

public interface ConstraintChecker {

    void check(Record record);
}
