package fr.awildelephant.gitrdbms.parser;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import static fr.awildelephant.gitrdbms.parser.rules.StatementRule.deriveStatementRule;

public final class Parser {

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public AST parse() {
        return deriveStatementRule(lexer);
    }
}
