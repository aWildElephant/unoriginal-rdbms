package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.server.QueryContext;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.storage.data.table.UpdateResultTable;
import fr.awildelephant.rdbms.version.TemporaryVersion;

public final class InsertIntoExecutor {

    private final ReadQueryExecutor readQueryExecutor;
    private final Storage storage;

    public InsertIntoExecutor(ReadQueryExecutor readQueryExecutor, Storage storage) {
        this.readQueryExecutor = readQueryExecutor;
        this.storage = storage;
    }

    // TODO: insert rows with context's temporary version as <from> timestamp
    public Table execute(InsertInto insertInto, QueryContext context) {
        context.setUpdate();

        final String tableName = insertInto.targetTable().name();
        final TemporaryVersion temporaryVersion = context.temporaryVersion();
        final Table content = readQueryExecutor.execute(insertInto.source(), temporaryVersion.databaseVersion());

        new Inserter(storage).insert(tableName, content, temporaryVersion);

        return new UpdateResultTable(content.numberOfTuples());
    }
}
