package fr.awildelephant.rdbms.schema;

public class ColumnNotFoundException extends RuntimeException {

    ColumnNotFoundException(String columnName) {
        super("Column not found: " + columnName);
    }

    ColumnNotFoundException(String tableName, String columnName) {
        super("Column not found: " + tableName + '.' + columnName);
    }
}
