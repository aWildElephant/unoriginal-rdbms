package fr.awildelephant.gitrdbms.lexer.tokens;

import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.INTEGER_LITERAL;

public final class IntegerLiteralToken implements Token {

    private final int value;

    public IntegerLiteralToken(int value) {
        this.value = value;
    }

    @Override
    public String text() {
        return "<integer literal>";
    }

    @Override
    public TokenType type() {
        return INTEGER_LITERAL;
    }

    public int value() {
        return value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerLiteralToken)) {
            return false;
        }

        final IntegerLiteralToken other = (IntegerLiteralToken) obj;

        return value == other.value;
    }

    @Override
    public String toString() {
        return "INTEGER_TOKEN(" + value + ")";
    }
}
