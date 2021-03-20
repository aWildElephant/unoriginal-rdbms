package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.*;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class TableFactory {

    private TableFactory() {

    }

    public static Table simpleTable(Schema schema) {
        return simpleTable(schema, 8);
    }

    public static Table simpleTable(Schema schema, int initialCapacity) {
        final List<Column> columns = createColumns(schema, initialCapacity);

        return new ColumnBasedTable(schema, columns);
    }

    public static List<Column> createColumns(Schema schema, int initialCapacity) {
        final List<Column> columns = new ArrayList<>(schema.numberOfAttributes());

        for (ColumnReference columnName : schema.columnNames()) {
            final ColumnMetadata columnMetadata = schema.column(columnName);

            final Column column = createColumn(columnMetadata, initialCapacity);

            columns.add(column);
        }
        return columns;
    }

    public static Column createColumn(ColumnMetadata columnMetadata, int initialCapacity) {
        final Domain columnType = columnMetadata.domain();
        final boolean nullable = !columnMetadata.notNull();
        switch (columnType) {
            case BOOLEAN:
                return new BooleanColumn(initialCapacity);
            case DATE:
                return new DateColumn(initialCapacity);
            case DECIMAL:
                return new DecimalColumn(initialCapacity);
            case INTEGER:
                if (nullable) {
                    return new IntegerColumn(initialCapacity);
                } else {
                    return new NonNullableIntegerColumn(initialCapacity);
                }
            case TEXT:
                return new TextColumn(initialCapacity);
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static TableWithChecker tableWithChecker(Schema schema) {
        return new TableWithChecker(simpleTable(schema));
    }
}
