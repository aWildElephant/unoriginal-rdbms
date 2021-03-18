package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.Explain;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.Explain.explain;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.EXPLAIN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;

final class ExplainStatementRule {

    private ExplainStatementRule() {

    }

    public static Explain deriveExplainStatement(Lexer lexer) {
        consumeAndExpect(EXPLAIN, lexer);

        return explain(deriveQueryExpression(lexer));
    }
}
