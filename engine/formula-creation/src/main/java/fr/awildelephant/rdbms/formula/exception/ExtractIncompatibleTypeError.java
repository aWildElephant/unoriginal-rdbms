package fr.awildelephant.rdbms.formula.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class ExtractIncompatibleTypeError extends DatabaseError {

    private final String field;
    private final String type;

    public ExtractIncompatibleTypeError(final String field, final String type) {
        this.field = field;
        this.type = type;
    }

    @Override
    public String getMessage() {
        return "Cannot extract " + field + " from incompatible type " + type;
    }
}
