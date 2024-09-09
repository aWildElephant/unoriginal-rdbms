package fr.awildelephant.rdbms.data.value.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class ValueOutOfBoundsError extends DatabaseError {

    private final Object value;

    public ValueOutOfBoundsError(Object value) {
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Value out of bounds: " + value;
    }
}
