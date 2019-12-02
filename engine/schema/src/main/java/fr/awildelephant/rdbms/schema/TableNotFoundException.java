package fr.awildelephant.rdbms.schema;

public final class TableNotFoundException extends RuntimeException {

    public TableNotFoundException(String name) {
        super("Table not found: " + name);
    }
}
