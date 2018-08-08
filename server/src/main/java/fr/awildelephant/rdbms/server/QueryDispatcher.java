package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.data.table.Table;

import static fr.awildelephant.rdbms.server.Inserter.insertRows;
import static fr.awildelephant.rdbms.server.TableCreator.tableFrom;

public class QueryDispatcher extends DefaultASTVisitor<Table> {

    private final Engine engine;
    private final Algebraizer algebraizer;

    QueryDispatcher(final Engine engine, final Algebraizer algebraizer) {
        this.engine = engine;
        this.algebraizer = algebraizer;
    }

    @Override
    public Table visit(CreateTable createTable) {
        final String tableName = createTable.tableName().name();

        checkTableDoesNotExist(tableName);

        engine.create(tableName, tableFrom(createTable));

        return null;
    }

    @Override
    public Table visit(DropTable dropTable) {
        engine.drop(dropTable.tableName().name());

        return null;
    }

    @Override
    public Table visit(InsertInto insertInto) {
        final Table table = engine.get(insertInto.targetTable().name());

        insertRows(insertInto, table);

        return null;
    }

    @Override
    public Table visit(Distinct distinct) {
        return executeReadQuery(distinct);
    }

    @Override
    public Table visit(Select select) {
        return executeReadQuery(select);
    }

    private Table executeReadQuery(AST ast) {
        return engine.execute(algebraizer.apply(ast));
    }

    @Override
    public Table defaultVisit(AST node) {
        throw new IllegalStateException();
    }

    private void checkTableDoesNotExist(String tableName) {
        if (engine.exists(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists");
        }
    }
}
