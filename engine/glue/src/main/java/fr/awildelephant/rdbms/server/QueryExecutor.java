package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.database.MVCC;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.parser.Parser;
import fr.awildelephant.rdbms.server.dispatch.QueryDispatcher;
import fr.awildelephant.rdbms.storage.data.table.Table;
import fr.awildelephant.rdbms.version.TemporaryVersion;
import fr.awildelephant.rdbms.version.TransactionId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fr.awildelephant.rdbms.lexer.InputStreamWrapper.wrap;
import static java.lang.System.currentTimeMillis;

public final class QueryExecutor {

    private static final Logger LOGGER = LogManager.getLogger("Monitoring");

    private final QueryDispatcher dispatcher;
    private final MVCC mvcc;
    private final TransactionManager transactionManager;

    QueryExecutor(QueryDispatcher dispatcher, MVCC mvcc, TransactionManager transactionManager) {
        this.dispatcher = dispatcher;
        this.mvcc = mvcc;
        this.transactionManager = transactionManager;
    }

    private static AST parse(final String query) {
        return new Parser(new Lexer(wrap(query))).parse();
    }

    public Table execute(String query) {
        LOGGER.trace("Executing \"{}\"", query);
        final long start = currentTimeMillis();
        final AST ast = parse(query);
        LOGGER.trace("Took {}ms to parse", currentTimeMillis() - start);

        final TransactionId transactionId = transactionManager.generate();

        TemporaryVersion version;
        boolean retry;
        Table result;
        do {
            version = new TemporaryVersion(mvcc.currentVersion(), transactionId);

            final QueryContext context = new QueryContext();
            context.temporaryVersion(version);

            result = dispatcher.dispatch(ast, context);
            retry = context.isUpdate() && !mvcc.commit(version);
        } while (retry);

        return result;
    }
}
