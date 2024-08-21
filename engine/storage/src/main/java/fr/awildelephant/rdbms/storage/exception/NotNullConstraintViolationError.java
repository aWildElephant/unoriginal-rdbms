package fr.awildelephant.rdbms.storage.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class NotNullConstraintViolationError extends DatabaseError {

    private final String columnName;

    public NotNullConstraintViolationError(final String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getMessage() {
        return "Cannot insert NULL in not-null column " + columnName;
    }
}
