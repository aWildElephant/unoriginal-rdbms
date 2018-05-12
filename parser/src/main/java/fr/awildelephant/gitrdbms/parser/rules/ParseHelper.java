package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.lexer.Lexer;
import fr.awildelephant.gitrdbms.lexer.tokens.IdentifierToken;
import fr.awildelephant.gitrdbms.lexer.tokens.IntegerLiteralToken;
import fr.awildelephant.gitrdbms.lexer.tokens.Token;
import fr.awildelephant.gitrdbms.lexer.tokens.TokenType;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.IDENTIFIER;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.INTEGER_LITERAL;
import static fr.awildelephant.gitrdbms.parser.error.ErrorHelper.unexpectedToken;

final class ParseHelper {

    private ParseHelper() {

    }

    static Token consumeAndExpect(final Lexer lexer, final TokenType expectedType) {
        final Token actual = lexer.consumeNextToken();

        if (actual.type() != expectedType) {
            throw unexpectedToken(actual, expectedType);
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
