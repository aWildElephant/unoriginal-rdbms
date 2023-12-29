package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.Truncate;
import fr.awildelephant.rdbms.data.value.VersionValue;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.server.QueryContext;
import fr.awildelephant.rdbms.storage.data.column.AppendableColumn;
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
        context.setUpdate(); // TODO: add parent class for executors that do updates

        final PermanentVersion readVersion = context.temporaryVersion().databaseVersion();
        final ManagedTable table = storage.get(truncate.tableName().name(), readVersion);

        final AppendableColumn fromVersionColumn = table.fromVersionColumn();
        final WriteableColumn toVersionColumn = table.toVersionColumn();

        final VersionValue endVersion = versionValue(context.temporaryVersion());

        final int toVersionColumnSize = toVersionColumn.size();

        int modifiedCount = 0;
        for (int i = 0; i < toVersionColumnSize; i++) {
            final Version fromVersion = fromVersionColumn.get(i).getVersion();
            final Version toVersion = toVersionColumn.get(i).getVersion();

            if (!fromVersion.isAfter(readVersion) && toVersion.isAfter(readVersion)) {
                toVersionColumn.set(i, endVersion);
                modifiedCount++;
            }
        }

        return new UpdateResultTable(modifiedCount);
    }
}
