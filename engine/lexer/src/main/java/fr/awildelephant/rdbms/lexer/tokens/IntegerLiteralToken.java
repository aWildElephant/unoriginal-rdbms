package fr.awildelephant.rdbms.lexer.tokens;

import java.math.BigInteger;

public record IntegerLiteralToken(BigInteger value) implements Token {

    /**
     * @param value must be not null
     */
    public IntegerLiteralToken {
    }

    @Override
    public String text() {
        return "<integer literal>";
    }

    @Override
    public TokenType type() {
        return TokenType.INTEGER_LITERAL;
    }

    @Override
    public String toString() {
        return "INTEGER_TOKEN(" + value + ")";
    }
}
