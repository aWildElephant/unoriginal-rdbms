package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.Table;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.parser.Parser;

import static fr.awildelephant.rdbms.lexer.InputStreamWrapper.wrap;

public final class RDBMS {

    private final QueryDispatcher dispatcher = new QueryDispatcher(new Engine(), new Algebraizer());

    private static AST parse(final String query) {
        return new Parser(new Lexer(wrap(query))).parse();
    }

    public Table execute(final String query) {
        final AST ast = parse(query);

        return dispatcher.apply(ast);
    }
}
