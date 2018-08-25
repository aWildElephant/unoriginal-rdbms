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
import static fr.awildelephant.rdbms.ast.DecimalLiteral.decimalLiteral;
import static fr.awildelephant.rdbms.ast.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.NullLiteral.nullLiteral;
import static fr.awildelephant.rdbms.ast.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

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
            case DECIMAL_LITERAL:
                return deriveDecimalLiteral(lexer);
            case INTEGER_LITERAL:
                return deriveIntegerLiteral(lexer);
            case TEXT_LITERAL:
                return deriveTextLiteral(lexer);
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
