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
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpression;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;

final class RowRule {

    private RowRule() {

    }

    static Row deriveRowRule(final Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);

        if (consumeIfNextTokenIs(RIGHT_PAREN, lexer)) {
            return row(List.of());
        }

        final List<AST> values = new LinkedList<>();
        values.add(deriveBooleanValueExpression(lexer));

        while (consumeIfNextTokenIs(COMMA, lexer)) {
            values.add(deriveBooleanValueExpression(lexer));
        }

        consumeAndExpect(RIGHT_PAREN, lexer);

        return row(values);
    }
}
