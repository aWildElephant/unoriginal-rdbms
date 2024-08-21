package fr.awildelephant.rdbms.server.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class ColumnCountMismatchError extends DatabaseError {

    private final int expected;
    private final int actual;

    public ColumnCountMismatchError(int expected, int actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String getMessage() {
        return "Column count mismatch: expected " + expected + " but got " + actual;
    }
}
