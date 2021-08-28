package fr.awildelephant.rdbms.lexer.tokens;

public final class IntegerLiteralToken implements Token {

    private final int value;

    /**
     * @param value must be not null
     */
    public IntegerLiteralToken(int value) {
        this.value = value;
    }

    public int value() {
        return value;
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
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final IntegerLiteralToken other)) {
            return false;
        }

        return value == other.value;
    }

    @Override
    public String toString() {
        return "INTEGER_TOKEN(" + value + ")";
    }
}
