package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.UpdateResultTable;
import org.jetbrains.annotations.Nullable;

import static fr.awildelephant.rdbms.server.TableCreator.tableFrom;

public final class QueryDispatcher {

    private final Storage storage;

    public QueryDispatcher(Storage storage) {
        this.storage = storage;
    }

    public Table dispatch(AST ast, QueryContext context) {
        if (ast instanceof final CreateTable createTable) {
            return executeCreateTable(createTable, context);
        } else if (ast instanceof final DropTable dropTable) {
            return executeDropTable(dropTable, context);
        } else if (ast instanceof final InsertInto insertInto) {
            return executeInsert(insertInto, context);
        } else {
            throw new UnsupportedOperationException("Cannot dispatch AST node " + ast.getClass().getSimpleName());
        }
    }

    @Nullable
    private Table executeCreateTable(CreateTable createTable, QueryContext context) {
        context.setUpdate();

        final String tableName = createTable.tableName().name();

        checkTableDoesNotExist(tableName);

        storage.create(tableName, tableFrom(createTable));

        return null;
    }

    private void checkTableDoesNotExist(String tableName) {
        if (storage.exists(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists");
        }
    }

    // TODO: version storage and drop table only from the context's timestamp
    private Table executeDropTable(DropTable dropTable, QueryContext context) {
        context.setUpdate();

        storage.drop(dropTable.tableName().name());

        return null;
    }

    private Table executeInsert(InsertInto insertInto, QueryContext context) {
        context.setUpdate();

        final String tableName = insertInto.targetTable().name();
        final Table content = dispatch(insertInto.source(), context);

        new Inserter(storage).insert(tableName, content);

        return new UpdateResultTable(content.numberOfTuples());
    }
}
