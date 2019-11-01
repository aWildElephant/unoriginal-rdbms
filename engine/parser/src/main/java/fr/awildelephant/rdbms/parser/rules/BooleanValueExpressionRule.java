package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.GreaterOrEqual.greaterOrEqual;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.ast.value.LessOrEqual.lessOrEqual;
import static fr.awildelephant.rdbms.ast.value.Like.like;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AND;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIKE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NOT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.ValueExpressionRule.deriveValueExpression;

final class BooleanValueExpressionRule {

    private BooleanValueExpressionRule() {

    }

    static AST deriveBooleanValueExpressionRule(final Lexer lexer) {
        final AST left = deriveTerm(lexer);

        if (nextTokenIs(OR, lexer)) {
            lexer.consumeNextToken();

            return or(left, deriveBooleanValueExpressionRule(lexer));
        }

        return left;
    }

    private static AST deriveTerm(final Lexer lexer) {
        final AST left = deriveFactor(lexer);

        if (nextTokenIs(AND, lexer)) {
            lexer.consumeNextToken();

            return and(left, deriveTerm(lexer));
        }

        return left;
    }

    private static AST deriveFactor(final Lexer lexer) {
        if (nextTokenIs(NOT, lexer)) {
            lexer.consumeNextToken();

            return not(deriveBooleanTest(lexer));
        }

        return deriveBooleanTest(lexer);
    }

    private static AST deriveBooleanTest(final Lexer lexer) {
        final AST left = deriveBooleanTestLeftInput(lexer);

        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case EQUAL:
                lexer.consumeNextToken();

                return equal(left, deriveValueExpression(lexer));
            case GREATER:
                lexer.consumeNextToken();

                return greater(left, deriveValueExpression(lexer));
            case GREATER_OR_EQUAL:
                lexer.consumeNextToken();

                return greaterOrEqual(left, deriveValueExpression(lexer));
            case LESS:
                lexer.consumeNextToken();

                return less(left, deriveValueExpression(lexer));
            case LESS_OR_EQUAL:
                lexer.consumeNextToken();

                return lessOrEqual(left, deriveValueExpression(lexer));
            case LIKE:
                lexer.consumeNextToken();

                return like(left, deriveValueExpression(lexer));
            case NOT:
                lexer.consumeNextToken();

                consumeAndExpect(LIKE, lexer);

                return not(like(left, deriveValueExpression(lexer)));
            default:
                return left;
        }
    }

    private static AST deriveBooleanTestLeftInput(final Lexer lexer) {
        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case LEFT_PAREN:
                lexer.consumeNextToken();

                final AST parenthesizedValueExpression = deriveBooleanValueExpressionRule(lexer);

                consumeAndExpect(RIGHT_PAREN, lexer);

                return parenthesizedValueExpression;
            case TRUE:
                lexer.consumeNextToken();

                return BooleanLiteral.TRUE;
            case FALSE:
                lexer.consumeNextToken();

                return BooleanLiteral.FALSE;
            case UNKNOWN:
                lexer.consumeNextToken();

                return BooleanLiteral.UNKNOWN;
            default:
                return deriveValueExpression(lexer);
        }
    }
}
