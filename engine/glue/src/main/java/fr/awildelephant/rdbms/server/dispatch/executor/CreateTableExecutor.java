package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.database.version.Version;
import fr.awildelephant.rdbms.server.QueryContext;

import static fr.awildelephant.rdbms.server.dispatch.executor.TableCreator.tableFrom;

public final class CreateTableExecutor {

    private final Storage storage;

    public CreateTableExecutor(Storage storage) {
        this.storage = storage;
    }

    public void execute(CreateTable createTable, QueryContext context) {
        context.setUpdate();

        final String tableName = createTable.tableName().name();

        checkTableDoesNotExist(tableName, context.temporaryVersion().databaseVersion());

        storage.create(tableName, tableFrom(createTable), context.temporaryVersion());
    }

    private void checkTableDoesNotExist(String tableName, Version version) {
        if (storage.exists(tableName, version)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists");
        }
    }
}
