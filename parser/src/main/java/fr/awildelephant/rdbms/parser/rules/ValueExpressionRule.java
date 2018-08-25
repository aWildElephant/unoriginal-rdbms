package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.TextLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.value.NullLiteral.nullLiteral;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.NumericValueExpressionRule.deriveNumericValueExpression;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;

final class ValueExpressionRule {

    private ValueExpressionRule() {

    }

    static AST deriveValueExpression(final Lexer lexer) {
        final TokenType nextType = lexer.lookupNextToken().type();

        switch (nextType) {
            case LEFT_PAREN:
                return deriveParenthesizedValueExpression(lexer);
            case NULL:
                lexer.consumeNextToken();

                return nullLiteral();
            case DATE:
                lexer.consumeNextToken();

                return cast(deriveTextLiteral(lexer), ColumnDefinition.DATE);
            case TEXT_LITERAL:
                return deriveTextLiteral(lexer);
            default:
                return deriveNumericValueExpression(lexer);
        }
    }

    private static AST deriveTextLiteral(final Lexer lexer) {
        final Token token = lexer.consumeNextToken();

        return textLiteral(((TextLiteralToken) token).content());
    }

    private static AST deriveParenthesizedValueExpression(final Lexer lexer) {
        lexer.consumeNextToken();

        final AST expression = deriveValueExpression(lexer);

        consumeAndExpect(RIGHT_PAREN, lexer);

        return expression;
    }
}
