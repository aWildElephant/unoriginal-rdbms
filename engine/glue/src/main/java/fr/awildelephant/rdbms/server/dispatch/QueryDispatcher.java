package fr.awildelephant.rdbms.server.dispatch;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.CreateView;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.ast.Explain;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.ast.Limit;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.Truncate;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.server.QueryContext;
import fr.awildelephant.rdbms.server.dispatch.executor.CreateTableExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.CreateViewExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.DropTableExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.ExplainExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.InsertIntoExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.ReadQueryExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.WithExecutor;

public final class QueryDispatcher {

    private final CreateTableExecutor createTableExecutor;
    private final CreateViewExecutor createViewExecutor;
    private final DropTableExecutor dropTableExecutor;
    private final ExplainExecutor explainExecutor;
    private final InsertIntoExecutor insertIntoExecutor;
    private final ReadQueryExecutor readQueryExecutor;
    private final WithExecutor withExecutor;

    public QueryDispatcher(CreateTableExecutor createTableExecutor, CreateViewExecutor createViewExecutor, DropTableExecutor dropTableExecutor, ExplainExecutor explainExecutor, InsertIntoExecutor insertIntoExecutor, ReadQueryExecutor readQueryExecutor, WithExecutor withExecutor) {
        this.createTableExecutor = createTableExecutor;
        this.createViewExecutor = createViewExecutor;
        this.dropTableExecutor = dropTableExecutor;
        this.explainExecutor = explainExecutor;
        this.insertIntoExecutor = insertIntoExecutor;
        this.readQueryExecutor = readQueryExecutor;
        this.withExecutor = withExecutor;
    }

    public Table dispatch(AST ast, QueryContext context) {
        if (ast instanceof final CreateTable createTable) {
            createTableExecutor.execute(createTable, context);
            return null;
        } else if (ast instanceof final CreateView createView) {
            createViewExecutor.execute(createView, context);
            return null;
        } else if (ast instanceof final DropTable dropTable) {
            dropTableExecutor.execute(dropTable, context);
            return null;
        } else if (ast instanceof final Explain explain) {
            return explainExecutor.execute(explain, context);
        } else if (ast instanceof final InsertInto insertInto) {
            return insertIntoExecutor.execute(insertInto, context);
        } else if (ast instanceof Truncate) {
            throw new UnsupportedOperationException("TRUNCATE statement not yet implemented");
        } else if (ast instanceof final With with) {
            return withExecutor.execute(with, context);
        } else if (isGenericReadQuery(ast)) {
            return readQueryExecutor.execute(ast, context);
        } else {
            throw new UnsupportedOperationException("Cannot dispatch AST node " + ast.getClass().getSimpleName());
        }
    }

    private static boolean isGenericReadQuery(AST ast) {
        return ast instanceof Distinct || ast instanceof Limit || ast instanceof Select || ast instanceof TableName || ast instanceof Values;
    }
}
