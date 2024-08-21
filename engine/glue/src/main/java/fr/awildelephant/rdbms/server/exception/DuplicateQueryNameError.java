package fr.awildelephant.rdbms.server.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class DuplicateQueryNameError extends DatabaseError {

    private final String name;

    public DuplicateQueryNameError(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Duplicate query name '" + name + "' in with clause";
    }
}
