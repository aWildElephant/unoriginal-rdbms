package fr.awildelephant.rdbms.engine.constraint;

import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;
import fr.awildelephant.rdbms.engine.data.record.Record;

public final class UniqueChecker implements ConstraintChecker {

    private final UniqueIndex index;

    public UniqueChecker(UniqueIndex index) {
        this.index = index;
    }

    @Override
    public void check(Record record) {
        if (index.conflictsWith(record)) {
            throw new IllegalArgumentException("Unique constraint violation");
        }
    }
}
