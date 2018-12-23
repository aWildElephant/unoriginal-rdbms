package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.parser.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fr.awildelephant.rdbms.lexer.InputStreamWrapper.wrap;
import static java.lang.System.currentTimeMillis;

public final class RDBMS {

    private static final Logger LOGGER = LogManager.getLogger("Monitoring");

    private final QueryDispatcher dispatcher;

    public RDBMS() {
        final Engine engine = new Engine();
        final Algebraizer algebraizer = new Algebraizer(engine);

        dispatcher = new QueryDispatcher(engine, algebraizer);
    }

    private static AST parse(final String query) {
        return new Parser(new Lexer(wrap(query))).parse();
    }

    public Table execute(final String query) {
        LOGGER.debug("Executing \"{}\"", query);
        final long start = currentTimeMillis();
        final AST ast = parse(query);
        LOGGER.trace("Took {}ms to parse", currentTimeMillis() - start);

        return dispatcher.apply(ast);
    }
}
