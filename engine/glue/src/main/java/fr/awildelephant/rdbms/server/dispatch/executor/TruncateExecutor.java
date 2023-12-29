package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.Truncate;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.server.QueryContext;
import fr.awildelephant.rdbms.storage.data.column.VersionColumn;
import fr.awildelephant.rdbms.storage.data.table.ManagedTable;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.UpdateResultTable;
import fr.awildelephant.rdbms.version.PermanentVersion;
import fr.awildelephant.rdbms.version.TemporaryVersion;
import fr.awildelephant.rdbms.version.Version;

public final class TruncateExecutor {

    private final Storage storage;

    public TruncateExecutor(final Storage storage) {
        this.storage = storage;
    }

    public Table execute(final Truncate truncate, final QueryContext context) {
        context.setUpdate(); // TODO: add parent class for executors that do updates

        final TemporaryVersion updateVersion = context.temporaryVersion();
        final PermanentVersion readVersion = updateVersion.databaseVersion();
        final ManagedTable table = storage.get(truncate.tableName().name(), readVersion);

        final VersionColumn fromVersionColumn = table.fromVersionColumn();
        final VersionColumn toVersionColumn = table.toVersionColumn();

        final int toVersionColumnSize = toVersionColumn.size();

        int modifiedCount = 0;
        for (int i = 0; i < toVersionColumnSize; i++) {
            final Version fromVersion = fromVersionColumn.getGeneric(i);
            final Version toVersion = toVersionColumn.getGeneric(i);

            if (!fromVersion.isAfter(readVersion) && toVersion.isAfter(readVersion)) {
                toVersionColumn.setGeneric(i, updateVersion);
                modifiedCount++;
            }
        }

        return new UpdateResultTable(modifiedCount);
    }
}
