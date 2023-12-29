package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.bitmap.BitSetBackedBitmap;
import fr.awildelephant.rdbms.storage.bitmap.Bitmap;
import fr.awildelephant.rdbms.storage.data.column.Column;
import fr.awildelephant.rdbms.storage.data.column.VersionColumn;
import fr.awildelephant.rdbms.storage.data.table.ColumnBasedTable;
import fr.awildelephant.rdbms.storage.data.table.FilteredTable;
import fr.awildelephant.rdbms.storage.data.table.ManagedTable;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.version.Version;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BaseTableOperator implements Operator {

    private final String tableName;
    private final Version version;

    public BaseTableOperator(final String tableName, final Version version) {
        this.tableName = tableName;
        this.version = version;
    }

    @Override
    public Table compute(final TemporaryStorage storage) {
        final ManagedTable inputTable = storage.getBaseTable(tableName);

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
    private static ColumnBasedTable removeSystemColumns(final Table inputTable) {
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

    private static Bitmap buildTemporalBitmap(final ManagedTable inputTable, final Version version) {
        final int inputSize = inputTable.numberOfTuples();
        final Bitmap bitmap = new BitSetBackedBitmap(inputSize);

        final VersionColumn fromVersionColumn = inputTable.fromVersionColumn();
        final VersionColumn toVersionColumn = inputTable.toVersionColumn();

        for (int i = 0; i < inputSize; i++) {
            final Version fromVersion = fromVersionColumn.getGeneric(i);
            final Version toVersion = toVersionColumn.getGeneric(i);

            if (!fromVersion.isAfter(version) && toVersion.isAfter(version)) {
                bitmap.set(i);
            }
        }

        return bitmap;
    }
}
