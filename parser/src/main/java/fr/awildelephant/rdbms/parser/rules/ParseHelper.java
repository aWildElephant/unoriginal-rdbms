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

    static boolean nextTokenIs(final TokenType expectedType, final Lexer lexer) {
        return lexer.lookupNextToken().type() == expectedType;
    }

    static Token consumeAndExpect(final TokenType expectedType, final Lexer lexer) {
        final Token actual = lexer.consumeNextToken();

        if (actual.type() != expectedType) {
            throw ErrorHelper.unexpectedToken(actual, expectedType);
        }

        return actual;
    }

    static String consumeIdentifier(final Lexer lexer) {
        return consumeAndExpect(IDENTIFIER, lexer).text();
    }

    static Integer consumeInteger(final Lexer lexer) {
        return ((IntegerLiteralToken) consumeAndExpect(INTEGER_LITERAL, lexer)).value();
    }
}
