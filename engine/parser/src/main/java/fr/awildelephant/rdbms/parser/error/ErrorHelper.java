package fr.awildelephant.rdbms.parser.error;

import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

public final class ErrorHelper {

    private ErrorHelper() {

    }

    public static IllegalStateException unexpectedToken(final Token token) {
        return new IllegalStateException("Unexpected token " + token);
    }

    public static IllegalStateException unexpectedToken(final Token actual, final TokenType expected) {
        return new IllegalStateException("Expected " + expected + " but got " + actual);
    }
}
