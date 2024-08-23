package fr.awildelephant.rdbms.parser.error;

import fr.awildelephant.rdbms.lexer.tokens.Token;
import fr.awildelephant.rdbms.lexer.tokens.TokenType;

public final class ErrorHelper {

    private ErrorHelper() {

    }

    public static UnexpectedTokenError unexpectedToken(final Token token) {
        return new UnexpectedTokenError(token);
    }

    public static UnexpectedTokenError unexpectedToken(final Token actual, final TokenType expected) {
        return new UnexpectedTokenError(actual, new TokenType[]{ expected });
    }

    public static UnexpectedTokenError unexpectedToken(Token actual, TokenType[] expected) {
        return new UnexpectedTokenError(actual, expected);
    }
}
