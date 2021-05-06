package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.engine.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.parser.Parser;
import fr.awildelephant.rdbms.server.explain.PlanJsonBuilder;
import fr.awildelephant.rdbms.server.with.WithInlinerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fr.awildelephant.rdbms.lexer.InputStreamWrapper.wrap;
import static java.lang.System.currentTimeMillis;

public final class RDBMS {

    private static final Logger LOGGER = LogManager.getLogger("Monitoring");

    private final ConcurrentQueryExecutor executor;

    public RDBMS() {
        final Storage storage = new Storage();
        final Algebraizer algebraizer = new Algebraizer(storage);
        final Optimizer optimizer = new Optimizer();
        final WithInlinerFactory withInlinerFactory = new WithInlinerFactory();
        final PlanJsonBuilder planJsonBuilder = new PlanJsonBuilder();

        final QueryDispatcher dispatcher = new QueryDispatcher(storage, algebraizer, optimizer, withInlinerFactory
        );
        executor = new ConcurrentQueryExecutor(dispatcher);
    }

    private static AST parse(final String query) {
        return new Parser(new Lexer(wrap(query))).parse();
    }

    public Table execute(final String query) {
        LOGGER.trace("Executing \"{}\"", query);
        final long start = currentTimeMillis();
        final AST ast = parse(query);
        LOGGER.trace("Took {}ms to parse", currentTimeMillis() - start);

        return executor.execute(ast);
    }
}
