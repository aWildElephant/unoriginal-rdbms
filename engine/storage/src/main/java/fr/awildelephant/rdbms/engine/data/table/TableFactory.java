package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.bitmap.BitSetBackedBitmap;
import fr.awildelephant.rdbms.engine.bitmap.Bitmap;
import fr.awildelephant.rdbms.engine.data.column.AppendableColumn;
import fr.awildelephant.rdbms.engine.data.column.BooleanColumn;
import fr.awildelephant.rdbms.engine.data.column.DateColumn;
import fr.awildelephant.rdbms.engine.data.column.DecimalColumn;
import fr.awildelephant.rdbms.engine.data.column.IntegerColumn;
import fr.awildelephant.rdbms.engine.data.column.LongColumn;
import fr.awildelephant.rdbms.engine.data.column.NonNullableIntegerColumn;
import fr.awildelephant.rdbms.engine.data.column.NonNullableLongColumn;
import fr.awildelephant.rdbms.engine.data.column.NullColumn;
import fr.awildelephant.rdbms.engine.data.column.TextColumn;
import fr.awildelephant.rdbms.engine.data.column.VersionColumn;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class TableFactory {

    private TableFactory() {

    }

    public static WriteableTable simpleTable(Schema schema) {
        return simpleTable(schema, 8);
    }

    public static WriteableTable simpleTable(Schema schema, int initialCapacity) {
        final List<AppendableColumn> columns = createColumns(schema, initialCapacity);

        return new ColumnBasedWriteableTable(schema, columns);
    }

    public static List<AppendableColumn> createColumns(Schema schema, int initialCapacity) {
        final List<AppendableColumn> columns = new ArrayList<>(schema.numberOfAttributes());

        for (ColumnReference columnName : schema.columnNames()) {
            final ColumnMetadata columnMetadata = schema.column(columnName).metadata();

            final AppendableColumn column = createColumn(columnMetadata, initialCapacity);

            columns.add(column);
        }
        return columns;
    }

    // TODO: use return switch
    public static AppendableColumn createColumn(ColumnMetadata columnMetadata, int initialCapacity) {
        final Domain columnType = columnMetadata.domain();
        final boolean nullable = !columnMetadata.notNull();
        switch (columnType) {
            case BOOLEAN -> {
                return new BooleanColumn(initialCapacity);
            }
            case DATE -> {
                return new DateColumn(initialCapacity);
            }
            case DECIMAL -> {
                return new DecimalColumn(initialCapacity);
            }
            case INTEGER -> {
                if (nullable) {
                    return new IntegerColumn(initialCapacity);
                } else {
                    return new NonNullableIntegerColumn(initialCapacity);
                }
            }
            case LONG -> {
                if (nullable) {
                    return new LongColumn(initialCapacity);
                } else {
                    return new NonNullableLongColumn(initialCapacity);
                }
            }
            case NULL -> {
                return new NullColumn();
            }
            case TEXT -> {
                return new TextColumn(initialCapacity);
            }
            case VERSION -> {
                return new VersionColumn(initialCapacity);
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    public static TableWithChecker tableWithChecker(Schema schema) {
        return new TableWithChecker(simpleTable(schema));
    }

    public static FilteredTable filter(Table inputTable, Predicate<Record> predicate) {
        return new FilteredTable(inputTable, predicateBitmap(inputTable, predicate));
    }

    private static Bitmap predicateBitmap(Table inputTable, Predicate<Record> predicate) {
        final Bitmap bitmap = new BitSetBackedBitmap(inputTable.numberOfTuples());
        int rowIndex = 0;
        for (Record record : inputTable) {
            if (predicate.test(record)) {
                bitmap.set(rowIndex);
            }
            rowIndex++;
        }
        return bitmap;
    }
}
