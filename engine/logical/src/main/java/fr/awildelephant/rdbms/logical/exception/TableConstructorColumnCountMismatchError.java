package fr.awildelephant.rdbms.logical.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class TableConstructorColumnCountMismatchError extends DatabaseError {

    @Override
    public String getMessage() {
        return "Column count does not match";
    }
}
