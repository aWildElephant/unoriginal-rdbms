package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.server.QueryContext;

public final class DropTableExecutor {

    private final Storage storage;

    public DropTableExecutor(Storage storage) {
        this.storage = storage;
    }

    public void execute(DropTable dropTable, QueryContext context) {
        context.setUpdate();

        storage.drop(dropTable.tableName().name());
    }
}
