package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.CreateView;
import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.UpdateResultTable;
import fr.awildelephant.rdbms.execution.AliasLop;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.server.TableCreator.tableFrom;

public final class QueryDispatcher {

    private final Algebraizer algebraizer;
    private final Storage storage;

    public QueryDispatcher(Algebraizer algebraizer, Storage storage) {
        this.algebraizer = algebraizer;
        this.storage = storage;
    }

    public Table dispatch(AST ast, QueryContext context) {
        if (ast instanceof final CreateTable createTable) {
            return executeCreateTable(createTable, context);
        } else if (ast instanceof final CreateView createView) {
            return executeCreateView(createView, context);
        } else if (ast instanceof final DropTable dropTable) {
            return executeDropTable(dropTable, context);
        } else if (ast instanceof final InsertInto insertInto) {
            return executeInsert(insertInto, context);
        } else {
            throw new UnsupportedOperationException("Cannot dispatch AST node " + ast.getClass().getSimpleName());
        }
    }

    private Table executeCreateTable(CreateTable createTable, QueryContext context) {
        context.setUpdate();

        final String tableName = createTable.tableName().name();

        checkTableDoesNotExist(tableName);

        storage.create(tableName, tableFrom(createTable));

        return null;
    }

    private Table executeCreateView(CreateView createView, QueryContext context) {
        context.setUpdate();

        final LogicalOperator query = algebraizer.apply(createView.query());

        final List<String> columnNames = createView.columnNames();
        final Schema querySchema = query.schema();
        if (querySchema.numberOfAttributes() != columnNames.size()) {
            throw new IllegalStateException();
        }

        final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();

        final List<ColumnReference> queryOutputColumns = querySchema.columnNames();
        for (int i = 0; i < queryOutputColumns.size(); i++) {
            columnAliasBuilder.add(queryOutputColumns.get(i), columnNames.get(i));
        }

        storage.createView(createView.name(), new AliasLop(query, columnAliasBuilder.build().orElseThrow()));

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
