package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.Truncate;
import fr.awildelephant.rdbms.data.value.VersionValue;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.schema.ReservedKeywords;
import fr.awildelephant.rdbms.server.QueryContext;
import fr.awildelephant.rdbms.storage.data.column.WriteableColumn;
import fr.awildelephant.rdbms.storage.data.table.ManagedTable;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.UpdateResultTable;
import fr.awildelephant.rdbms.version.PermanentVersion;
import fr.awildelephant.rdbms.version.Version;

import static fr.awildelephant.rdbms.data.value.VersionValue.versionValue;

public final class TruncateExecutor {

    private final Storage storage;

    public TruncateExecutor(Storage storage) {
        this.storage = storage;
    }

    public Table execute(Truncate truncate, QueryContext context) {
        final PermanentVersion readVersion = context.temporaryVersion().databaseVersion();
        final ManagedTable table = storage.get(truncate.tableName().name(), readVersion);

        // FIXME: cast dégueulasse
        final WriteableColumn toVersionColumn = (WriteableColumn) table.columns().get(table.schema().indexOf(ReservedKeywords.TO_VERSION_COLUMN));

        final VersionValue endVersion = versionValue(context.temporaryVersion());

        final int toVersionColumnSize = toVersionColumn.size();

        int modifiedCount = 0;
        for (int i = 0; i < toVersionColumnSize; i++) {
            final Version currentVersion = toVersionColumn.get(i).getVersion();

            if (!currentVersion.isAfter(readVersion)) {
                toVersionColumn.set(i, endVersion);
                modifiedCount++;
            }
        }

        return new UpdateResultTable(modifiedCount);
    }
}
