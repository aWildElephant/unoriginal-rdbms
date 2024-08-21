package fr.awildelephant.rdbms.storage.constraint;

import fr.awildelephant.rdbms.storage.data.index.UniqueIndex;
import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.exception.UniqueConstraintViolationError;

public final class UniqueChecker implements ConstraintChecker {

    private final UniqueIndex index;

    public UniqueChecker(UniqueIndex index) {
        this.index = index;
    }

    @Override
    public void check(Record record) {
        if (index.conflictsWith(record)) {
            throw new UniqueConstraintViolationError();
        }
    }
}
