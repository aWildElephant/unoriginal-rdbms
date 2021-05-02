package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.IDENTIFIER;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;

final class ParseHelper {

    private ParseHelper() {

    }

    static boolean nextTokenIs(final TokenType expectedType, final Lexer lexer) {
        return lexer.lookupNextToken().type() == expectedType;
    }

    static boolean consumeIfNextTokenIs(TokenType expectedType, Lexer lexer) {
        if (nextTokenIs(expectedType, lexer)) {
            lexer.consumeNextToken();

            return true;
        } else {
            return false;
        }
    }

    static Token consumeAndExpect(final TokenType expectedType, final Lexer lexer) {
        final Token actual = lexer.consumeNextToken();

        if (actual.type() != expectedType) {
            throw unexpectedToken(actual, expectedType);
        }

        return actual;
    }

    static String consumeIdentifier(final Lexer lexer) {
        return consumeAndExpect(IDENTIFIER, lexer).text();
    }
}
