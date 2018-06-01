package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;
import fr.awildelephant.rdbms.parser.error.ErrorHelper;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IDENTIFIER;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER_LITERAL;

final class ParseHelper {

    private ParseHelper() {

    }

    static Token consumeAndExpect(final Lexer lexer, final TokenType expectedType) {
        final Token actual = lexer.consumeNextToken();

        if (actual.type() != expectedType) {
            throw ErrorHelper.unexpectedToken(actual, expectedType);
        }

        return actual;
    }

    static String consumeIdentifier(final Lexer lexer) {
        return consumeAndExpect(lexer, IDENTIFIER).text();
    }

    static Integer consumeInteger(final Lexer lexer) {
        return ((IntegerLiteralToken) consumeAndExpect(lexer, INTEGER_LITERAL)).value();
    }
}
