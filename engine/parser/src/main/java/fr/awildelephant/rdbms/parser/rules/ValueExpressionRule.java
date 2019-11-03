package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.value.IntervalGranularity;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.DecimalLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.TextLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.value.Avg.avg;
import static fr.awildelephant.rdbms.ast.value.CaseWhen.caseWhen;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.DecimalLiteral.decimalLiteral;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.ExtractYear.extractYear;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.IntervalGranularity.DAY_GRANULARITY;
import static fr.awildelephant.rdbms.ast.value.IntervalGranularity.MONTH_GRANULARITY;
import static fr.awildelephant.rdbms.ast.value.IntervalGranularity.YEAR_GRANULARITY;
import static fr.awildelephant.rdbms.ast.value.IntervalLiteral.intervalLiteral;
import static fr.awildelephant.rdbms.ast.value.Min.min;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.ast.value.NullLiteral.nullLiteral;
import static fr.awildelephant.rdbms.ast.value.Placeholder.placeholder;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;
import static fr.awildelephant.rdbms.ast.value.Sum.sum;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASTERISK;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ELSE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.END;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER_LITERAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TEXT_LITERAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.THEN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.WHEN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.YEAR;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.BooleanValueExpressionRule.deriveBooleanValueExpressionRule;
import static fr.awildelephant.rdbms.parser.rules.ColumnReferenceRule.deriveColumnReference;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.nextTokenIs;
import static fr.awildelephant.rdbms.parser.rules.QueryExpressionRule.deriveQueryExpression;

final class ValueExpressionRule {

    private ValueExpressionRule() {

    }

    static AST deriveValueExpression(final Lexer lexer) {
        final TokenType nextType = lexer.lookupNextToken().type();

        switch (nextType) {
            case TEXT_LITERAL:
                return deriveTextLiteral(lexer);
            case MINUS:
                // TODO: très mauvais
                lexer.consumeNextToken();

                final Token numericValueToNegate = lexer.consumeNextToken();

                switch (numericValueToNegate.type()) {
                    case INTEGER_LITERAL:
                        final IntegerLiteralToken integerValue = (IntegerLiteralToken) numericValueToNegate;

                        return integerLiteral(-integerValue.value());
                    case DECIMAL_LITERAL:
                        final DecimalLiteralToken decimalValue = (DecimalLiteralToken) numericValueToNegate;

                        return decimalLiteral(decimalValue.value().negate());
                    default:
                        throw unexpectedToken(numericValueToNegate);
                }
            default:
                return deriveNumericValueExpression(lexer);
        }
    }

    private static AST deriveNumericValueExpression(final Lexer lexer) {
        final AST left = deriveTerm(lexer);

        switch (lexer.lookupNextToken().type()) {
            case PLUS:
                lexer.consumeNextToken();

                return plus(left, deriveNumericValueExpression(lexer));
            case MINUS:
                lexer.consumeNextToken();

                return minus(left, deriveNumericValueExpression(lexer));
            default:
                return left;
        }
    }

    private static AST deriveTerm(Lexer lexer) {
        final AST left = deriveFactor(lexer);

        final TokenType nextType = lexer.lookupNextToken().type();
        switch (nextType) {
            case ASTERISK:
                lexer.consumeNextToken();

                return multiply(left, deriveTerm(lexer));
            case SOLIDUS:
                lexer.consumeNextToken();

                return divide(left, deriveTerm(lexer));
            default:
                return left;
        }
    }

    private static AST deriveFactor(Lexer lexer) {
        final Token nextToken = lexer.lookupNextToken();

        switch (nextToken.type()) {
            case CASE:
                lexer.consumeNextToken();

                consumeAndExpect(WHEN, lexer);

                final AST condition = deriveBooleanValueExpressionRule(lexer);

                consumeAndExpect(THEN, lexer);

                final AST thenExpression = deriveValueExpression(lexer);

                consumeAndExpect(ELSE, lexer);

                final AST elseExpression = deriveValueExpression(lexer);

                consumeAndExpect(END, lexer);

                return caseWhen(condition, thenExpression, elseExpression);
            case LEFT_PAREN:
                return deriveParenthesizedValueExpression(lexer);
            case DATE:
                return deriveDateValueExpression(lexer);
            case INTERVAL:
                lexer.consumeNextToken();

                final String intervalString = ((TextLiteralToken) consumeAndExpect(TEXT_LITERAL, lexer)).content();

                final Token yetAnotherToken = lexer.consumeNextToken();
                final IntervalGranularity granularity;
                switch (yetAnotherToken.type()) {
                    case DAY:
                        granularity = DAY_GRANULARITY;
                        break;
                    case MONTH:
                        granularity = MONTH_GRANULARITY;
                        break;
                    case YEAR:
                        granularity = YEAR_GRANULARITY;
                        break;
                    default:
                        throw unexpectedToken(yetAnotherToken);
                }

                Integer precision = null;

                if (nextTokenIs(LEFT_PAREN, lexer)) {
                    lexer.consumeNextToken();

                    precision = ((IntegerLiteralToken) consumeAndExpect(INTEGER_LITERAL, lexer)).value();

                    consumeAndExpect(RIGHT_PAREN, lexer);
                }

                return intervalLiteral(intervalString, granularity, precision);
            case DECIMAL_LITERAL:
                return deriveDecimalLiteral(lexer);
            case EXTRACT:
                lexer.consumeNextToken();

                consumeAndExpect(LEFT_PAREN, lexer);
                consumeAndExpect(YEAR, lexer);
                consumeAndExpect(FROM, lexer);
                final AST date = deriveValueExpression(lexer);
                consumeAndExpect(RIGHT_PAREN, lexer);

                return extractYear(date);
            case INTEGER_LITERAL:
                return deriveIntegerLiteral(lexer);
            case NULL:
                lexer.consumeNextToken();

                return nullLiteral();
            case AVG:
                lexer.consumeNextToken();
                consumeAndExpect(LEFT_PAREN, lexer);

                final AST avgInput = deriveValueExpression(lexer);

                consumeAndExpect(RIGHT_PAREN, lexer);

                return avg(avgInput);
            case COUNT:
                lexer.consumeNextToken();
                consumeAndExpect(LEFT_PAREN, lexer);
                consumeAndExpect(ASTERISK, lexer);
                consumeAndExpect(RIGHT_PAREN, lexer);

                return countStar();
            case MIN:
                lexer.consumeNextToken();
                consumeAndExpect(LEFT_PAREN, lexer);

                final AST minInput = deriveValueExpression(lexer);

                consumeAndExpect(RIGHT_PAREN, lexer);

                return min(minInput);
            case SUM:
                lexer.consumeNextToken();
                consumeAndExpect(LEFT_PAREN, lexer);

                final AST sumInput = deriveValueExpression(lexer);

                consumeAndExpect(RIGHT_PAREN, lexer);

                return sum(sumInput);
            case IDENTIFIER:
                return deriveColumnReference(lexer);
            case QUESTION_MARK:
                lexer.consumeNextToken();

                return placeholder();
            default:
                throw unexpectedToken(nextToken);
        }
    }

    private static AST deriveDateValueExpression(Lexer lexer) {
        consumeAndExpect(DATE, lexer);

        return cast(deriveTextLiteral(lexer), ColumnDefinition.DATE);
    }

    private static AST deriveParenthesizedValueExpression(Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);

        final AST input;
        if (nextTokenIs(SELECT, lexer)) {
            input = deriveQueryExpression(lexer);
        } else {
            input = deriveValueExpression(lexer);
        }

        consumeAndExpect(RIGHT_PAREN, lexer);

        return input;
    }

    private static AST deriveDecimalLiteral(Lexer lexer) {
        final Token token = lexer.consumeNextToken();

        return decimalLiteral(((DecimalLiteralToken) token).value());
    }

    private static AST deriveIntegerLiteral(Lexer lexer) {
        final Token token = lexer.consumeNextToken();

        return integerLiteral(((IntegerLiteralToken) token).value());
    }

    private static AST deriveTextLiteral(final Lexer lexer) {
        final Token token = lexer.consumeNextToken();

        return textLiteral(((TextLiteralToken) token).content());
    }
}
