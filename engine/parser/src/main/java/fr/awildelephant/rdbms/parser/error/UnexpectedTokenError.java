package fr.awildelephant.rdbms.parser.error;

import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UnexpectedTokenError extends ParsingError {

    private final Token actual;
    private final TokenType[] expected;

    public UnexpectedTokenError(final Token actual) {
        this(actual, null);
    }

    public UnexpectedTokenError(final Token actual, final TokenType[] expected) {
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public String getMessage() {
        if (expected == null || expected.length == 0) {
            return "Unexpected token " + actual;
        } else if (expected.length == 1) {
            return "Expected " + expected[0] + " but got " + actual;
        } else {
            final String expectedString = Stream.of(expected)
                    .map(Objects::toString)
                    .collect(Collectors.joining(", "));

            return "Expected one of [" + expectedString + "] but got " + actual;
        }
    }
}
