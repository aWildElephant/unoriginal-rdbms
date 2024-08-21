package fr.awildelephant.rdbms.schema;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class ColumnNotFoundError extends DatabaseError {


    private final String tableName;
    private final String columnName;

    ColumnNotFoundError(final String columnName) {
        this(null, columnName);
    }

    ColumnNotFoundError(final String tableName, final String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    @Override
    public String getMessage() {
        final StringBuilder stringBuilder = new StringBuilder("Column not found: ");
        if (tableName != null) {
         stringBuilder.append(tableName).append('.');
        }
        return stringBuilder.append(columnName).toString();
    }
}
