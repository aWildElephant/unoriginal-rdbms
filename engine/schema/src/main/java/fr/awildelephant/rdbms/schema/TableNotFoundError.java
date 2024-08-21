package fr.awildelephant.rdbms.schema;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class TableNotFoundError extends DatabaseError {

    private final String name;

    public TableNotFoundError(final String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Table not found: " + name;
    }
}
