package fr.awildelephant.rdbms.storage.constraint;

import fr.awildelephant.rdbms.storage.data.record.Record;

import java.util.Collection;
import java.util.LinkedList;

public final class CompositeChecker implements ConstraintChecker {

    private final Collection<ConstraintChecker> checkers = new LinkedList<>();

    public void add(ConstraintChecker checker) {
        checkers.add(checker);
    }

    @Override
    public void check(Record record) {
        for (ConstraintChecker checker : checkers) {
            checker.check(record);
        }
    }
}
