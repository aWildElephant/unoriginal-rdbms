package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.ReservedKeywords;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.bitmap.BitSetBackedBitmap;
import fr.awildelephant.rdbms.storage.bitmap.Bitmap;
import fr.awildelephant.rdbms.storage.data.column.Column;
import fr.awildelephant.rdbms.storage.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.storage.data.table.FilteredTable;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.version.Version;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseTableOperator implements Operator {

    private final String tableName;
    private final Version version;

    public BaseTableOperator(String tableName, Version version) {
        this.tableName = tableName;
        this.version = version;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table inputTable = storage.get(tableName);

        final Bitmap bitmap = buildTemporalBitmap(inputTable, version);

        Table result = removeSystemColumns(inputTable);

        final boolean needFilter = bitmap.cardinality() != inputTable.numberOfTuples();
        if (needFilter) {
            // TODO: optimizer should decide whether we materialize a new table or not
            result = new FilteredTable(result, bitmap);
        }

        return result;
    }

    @NotNull
    private static ColumnBasedTable removeSystemColumns(Table inputTable) {
        // FIXME: code duplicated with ProjectOperator
        final Schema inputSchema = inputTable.schema();
        final Schema outputSchema = inputSchema.removeSystemColumns();
        final List<? extends Column> inputColumns = inputTable.columns();

        final List<Column> outputColumns = new ArrayList<>(outputSchema.numberOfAttributes());

        for (ColumnReference columnReference : outputSchema.columnNames()) {
            outputColumns.add(inputColumns.get(inputSchema.column(columnReference).index()));
        }
        return new ColumnBasedTable(outputSchema, outputColumns);
    }

    private static Bitmap buildTemporalBitmap(Table inputTable, Version version) {
        final int inputSize = inputTable.numberOfTuples();
        final Bitmap bitmap = new BitSetBackedBitmap(inputSize);

        final List<? extends Column> inputColumn = inputTable.columns();
        final Column fromVersionColumn = inputColumn.get(inputTable.schema().indexOf(ReservedKeywords.FROM_VERSION_COLUMN));
        final Column toVersionColumn = inputColumn.get(inputTable.schema().indexOf(ReservedKeywords.TO_VERSION_COLUMN));

        for (int i = 0; i < inputSize; i++) {
            final Version fromVersion = fromVersionColumn.get(i).getVersion();
            final Version toVersion = toVersionColumn.get(i).getVersion();

            if (!fromVersion.isAfter(version) && toVersion.isAfter(version)) {
                bitmap.set(i);
            }
        }

        return bitmap;
    }
}
