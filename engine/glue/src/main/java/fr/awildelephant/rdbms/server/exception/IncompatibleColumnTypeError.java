package fr.awildelephant.rdbms.server.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public class IncompatibleColumnTypeError extends DatabaseError {

    private final String columnName;
    private final String sourceType;
    private final String destinationType;

    public IncompatibleColumnTypeError(String columnName, String sourceType, String destinationType) {
        this.columnName = columnName;
        this.sourceType = sourceType;
        this.destinationType = destinationType;
    }

    @Override
    public String getMessage() {
        return "Column '" + columnName + "': type " + sourceType + " is incompatible with type " + destinationType;
    }
}
