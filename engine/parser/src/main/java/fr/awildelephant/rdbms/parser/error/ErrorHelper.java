package fr.awildelephant.rdbms.parser.error;

import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ErrorHelper {

    private ErrorHelper() {

    }

    public static IllegalStateException unexpectedToken(final Token token) {
        return new IllegalStateException("Unexpected token " + token);
    }

    public static IllegalStateException unexpectedToken(final Token actual, final TokenType expected) {
        return new IllegalStateException("Expected " + expected + " but got " + actual);
    }

    public static IllegalStateException unexpectedToken(Token actual, TokenType[] expected) {
        final String expectedString = Stream.of(expected)
                                            .map(Objects::toString)
                                            .collect(Collectors.joining(", "));

        return new IllegalStateException("Expected one of [" + expectedString + "] but got " + actual);
    }
}
