package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.LexerSnapshot;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.parser.error.ParsingError;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Exists.exists;
import static fr.awildelephant.rdbms.ast.InValueList.inValueList;
import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.Between.between;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.falseLiteral;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.trueLiteral;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.unknownLiteral;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.GreaterOrEqual.greaterOrEqual;
import static fr.awildelephant.rdbms.ast.value.In.in;
import static fr.awildelephant.rdbms.ast.value.IsNull.isNull;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.ast.value.LessOrEqual.lessOrEqual;
import static fr.awildelephant.rdbms.ast.value.Like.like;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.NotEqual.notEqual;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.AND;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IS;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LIKE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NOT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.OR;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;
import static fr.awildelephant.rdbms.parser.rules.QuerySpecificationRule.deriveQuerySpecificationRule;
import static fr.awildelephant.rdbms.parser.rules.ValueExpressionRule.deriveValueExpression;

final class BooleanValueExpressionRule {

    private BooleanValueExpressionRule() {

    }

    static AST deriveBooleanValueExpression(final Lexer lexer) {
        final AST left = deriveTerm(lexer);

        if (consumeIfNextTokenIs(OR, lexer)) {
            return or(left, deriveBooleanValueExpression(lexer));
        }

        return left;
    }

    private static AST deriveTerm(final Lexer lexer) {
        final AST left = deriveFactor(lexer);

        if (consumeIfNextTokenIs(AND, lexer)) {
            return and(left, deriveTerm(lexer));
        }

        return left;
    }

    private static AST deriveFactor(final Lexer lexer) {
        if (consumeIfNextTokenIs(NOT, lexer)) {
            return not(deriveBooleanTest(lexer));
        }

        return deriveBooleanTest(lexer);
    }

    private static AST deriveBooleanTest(final Lexer lexer) {
        final AST left = deriveBooleanTestLeftInput(lexer);

        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case BETWEEN -> {
                lexer.consumeNextToken();
                final AST lowerBound = deriveValueExpression(lexer);
                consumeAndExpect(AND, lexer);
                final AST upperBound = deriveValueExpression(lexer);
                return between(left, lowerBound, upperBound);
            }
            case EQUAL -> {
                lexer.consumeNextToken();
                return equal(left, deriveValueExpression(lexer));
            }
            case GREATER -> {
                lexer.consumeNextToken();
                return greater(left, deriveValueExpression(lexer));
            }
            case GREATER_OR_EQUAL -> {
                lexer.consumeNextToken();
                return greaterOrEqual(left, deriveValueExpression(lexer));
            }
            case IN -> {
                return deriveInPredicate(left, lexer);
            }
            case IS -> {
                return deriveIsPredicate(left, lexer);
            }
            case LESS -> {
                lexer.consumeNextToken();
                return less(left, deriveValueExpression(lexer));
            }
            case LESS_OR_EQUAL -> {
                lexer.consumeNextToken();
                return lessOrEqual(left, deriveValueExpression(lexer));
            }
            case LIKE -> {
                lexer.consumeNextToken();
                return like(left, deriveValueExpression(lexer));
            }
            case NOT -> {
                lexer.consumeNextToken();
                if (nextTokenIs(IN, lexer)) {
                    return not(deriveInPredicate(left, lexer));
                } else {
                    consumeAndExpect(LIKE, lexer);

                    return not(like(left, deriveValueExpression(lexer)));
                }
            }
            case NOT_EQUAL -> {
                lexer.consumeNextToken();
                return notEqual(left, deriveValueExpression(lexer));
            }
            default -> {
                return left;
            }
        }
    }

    private static AST deriveInPredicate(AST left, Lexer lexer) {
        lexer.consumeNextToken();

        final LexerSnapshot lexerSnapshot = lexer.save();
        try {
            return in(left, deriveInSubquery(lexer));
        } catch (ParsingError e) {
            lexer.restore(lexerSnapshot);

            return in(left, deriveInPredicateValueList(lexer));
        }
    }

    private static AST deriveInSubquery(Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);

        final AST subquery = deriveQuerySpecificationRule(lexer);

        consumeAndExpect(RIGHT_PAREN, lexer);

        return subquery;
    }

    private static AST deriveInPredicateValueList(Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);

        final List<AST> values = new ArrayList<>();

        do {
            values.add(deriveBooleanValueExpression(lexer));
        } while (consumeIfNextTokenIs(COMMA, lexer));

        consumeAndExpect(RIGHT_PAREN, lexer);

        return inValueList(values);
    }

    private static AST deriveIsPredicate(AST left, Lexer lexer) {
        consumeAndExpect(IS, lexer);

        final boolean negate = consumeIfNextTokenIs(NOT, lexer);

        consumeAndExpect(NULL, lexer);

        return negate ? not(isNull(left)) : isNull(left);
    }

    private static AST deriveBooleanTestLeftInput(final Lexer lexer) {
        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case LEFT_PAREN -> {
                lexer.consumeNextToken();
                final AST parenthesizedValueExpression = deriveBooleanValueExpression(lexer);
                consumeAndExpect(RIGHT_PAREN, lexer);
                return parenthesizedValueExpression;
            }
            case EXISTS -> {
                lexer.consumeNextToken();
                consumeAndExpect(LEFT_PAREN, lexer);
                final AST input = deriveQueryExpression(lexer);
                consumeAndExpect(RIGHT_PAREN, lexer);
                return exists(input);
            }
            case TRUE -> {
                lexer.consumeNextToken();
                return trueLiteral();
            }
            case FALSE -> {
                lexer.consumeNextToken();
                return falseLiteral();
            }
            case UNKNOWN -> {
                lexer.consumeNextToken();
                return unknownLiteral();
            }
            default -> {
                return deriveValueExpression(lexer);
            }
        }
    }
}
