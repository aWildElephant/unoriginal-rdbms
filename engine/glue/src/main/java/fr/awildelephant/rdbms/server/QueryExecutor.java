package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.parser.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static fr.awildelephant.rdbms.lexer.InputStreamWrapper.wrap;
import static java.lang.System.currentTimeMillis;

public final class QueryExecutor {

    private static final Logger LOGGER = LogManager.getLogger("Monitoring");

    private final QueryDispatcher dispatcher;
    private final Lock lock = new ReentrantLock();

    QueryExecutor(QueryDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    private static AST parse(final String query) {
        return new Parser(new Lexer(wrap(query))).parse();
    }

    public Table execute(String query) {
        lock.lock();

        LOGGER.trace("Executing \"{}\"", query);
        final long start = currentTimeMillis();
        final AST ast = parse(query);
        LOGGER.trace("Took {}ms to parse", currentTimeMillis() - start);

        try {
            return dispatcher.apply(ast);
        } finally {
            lock.unlock();
        }
    }
}
