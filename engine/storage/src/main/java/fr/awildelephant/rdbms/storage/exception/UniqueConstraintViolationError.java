package fr.awildelephant.rdbms.storage.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public class UniqueConstraintViolationError extends DatabaseError {

    @Override
    public String getMessage() {
        return "Unique constraint violation";
    }
}
