package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.DecimalLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.TextLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.DecimalLiteral.decimalLiteral;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.ast.value.NullLiteral.nullLiteral;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.ASTERISK;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class ValueExpressionRule {

    private ValueExpressionRule() {

    }

    static AST deriveValueExpression(final Lexer lexer) {
        final TokenType nextType = lexer.lookupNextToken().type();

        switch (nextType) {
            case COUNT:
                lexer.consumeNextToken();
                consumeAndExpect(LEFT_PAREN, lexer);
                consumeAndExpect(ASTERISK, lexer);
                consumeAndExpect(RIGHT_PAREN, lexer);

                return countStar();
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
        final TokenType nextType = lexer.lookupNextToken().type();

        switch (nextType) {
            case DECIMAL_LITERAL:
                return deriveDecimalLiteral(lexer);
            case INTEGER_LITERAL:
                return deriveIntegerLiteral(lexer);
            case LEFT_PAREN:
                return deriveParenthesizedValueExpression(lexer);
            default:
                return columnName(consumeIdentifier(lexer));
        }
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

    private static AST deriveParenthesizedValueExpression(final Lexer lexer) {
        lexer.consumeNextToken();

        final AST expression = deriveValueExpression(lexer);

        consumeAndExpect(RIGHT_PAREN, lexer);

        return expression;
    }
}
