package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.ast.SortedSelect;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.engine.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;

import static fr.awildelephant.rdbms.server.Inserter.insertRows;
import static fr.awildelephant.rdbms.server.TableCreator.tableFrom;

public class QueryDispatcher extends DefaultASTVisitor<Table> {

    private final Storage storage;
    private final Algebraizer algebraizer;

    QueryDispatcher(final Storage storage, final Algebraizer algebraizer) {
        this.storage = storage;
        this.algebraizer = algebraizer;
    }

    @Override
    public Table visit(CreateTable createTable) {
        final String tableName = createTable.tableName().name();

        checkTableDoesNotExist(tableName);

        storage.create(tableName, tableFrom(createTable));

        return null;
    }

    @Override
    public Table visit(DropTable dropTable) {
        storage.drop(dropTable.tableName().name());

        return null;
    }

    @Override
    public Table visit(InsertInto insertInto) {
        final Table table = storage.get(insertInto.targetTable().name());

        insertRows(insertInto, table);

        return null;
    }

    @Override
    public Table visit(Distinct distinct) {
        return executeReadQuery(distinct);
    }

    @Override
    public Table visit(SortedSelect sortedSelect) {
        return executeReadQuery(sortedSelect);
    }

    @Override
    public Table visit(TableName tableName) {
        return executeReadQuery(tableName);
    }

    @Override
    public Table visit(Values values) {
        return executeReadQuery(values);
    }

    private Table executeReadQuery(AST ast) {
        return storage.execute(algebraizer.apply(ast));
    }

    @Override
    public Table defaultVisit(AST node) {
        throw new IllegalStateException();
    }

    private void checkTableDoesNotExist(String tableName) {
        if (storage.exists(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists");
        }
    }
}
