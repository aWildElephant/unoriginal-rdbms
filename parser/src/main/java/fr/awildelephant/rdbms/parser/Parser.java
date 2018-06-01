package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.parser.rules.StatementRule.deriveStatementRule;

public final class Parser {

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    AST parse() {
        return deriveStatementRule(lexer);
    }
}
