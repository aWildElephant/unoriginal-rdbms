package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ValueExpressionRule.deriveValueExpression;

final class RowRule {

    private RowRule() {

    }

    static Row deriveRowRule(final Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);

        final List<AST> values = new LinkedList<>();
        values.add(deriveValueExpression(lexer));

        while (lexer.lookupNextToken().type() == COMMA) {
            lexer.consumeNextToken();

            values.add(deriveValueExpression(lexer));
        }

        consumeAndExpect(RIGHT_PAREN, lexer);

        return row(values);
    }
}
