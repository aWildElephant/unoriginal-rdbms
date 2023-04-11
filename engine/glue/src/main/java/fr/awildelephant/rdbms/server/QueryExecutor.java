package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.database.MVCC;
import fr.awildelephant.rdbms.database.TransactionId;
import fr.awildelephant.rdbms.database.version.TemporaryVersion;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.parser.Parser;
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

    // TODO: retry if update fails to commit. In order to do that do we have to separate execute and executeUpdate?
    public Table execute(String query) {
        LOGGER.trace("Executing \"{}\"", query);
        final long start = currentTimeMillis();
        final AST ast = parse(query);
        LOGGER.trace("Took {}ms to parse", currentTimeMillis() - start);

        final TransactionId transactionId = transactionManager.generate();

        final TemporaryVersion version = new TemporaryVersion(mvcc.currentVersion(), transactionId);

        final Table result = dispatcher.apply(ast);

        if (!mvcc.commit(version)) {
            throw new UnsupportedOperationException("Query retry is not supported");
        }

        return result;
    }
}
