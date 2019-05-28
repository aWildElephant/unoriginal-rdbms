package fr.awildelephant.rdbms.lexer.tokens;

import java.math.BigDecimal;
import java.util.Objects;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DECIMAL_LITERAL;

public final class DecimalLiteralToken implements Token {

    private final BigDecimal value;

    public DecimalLiteralToken(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public String text() {
        return "<decimal literal>";
    }

    @Override
    public TokenType type() {
        return DECIMAL_LITERAL;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DecimalLiteralToken)) {
            return false;
        }

        final DecimalLiteralToken other = (DecimalLiteralToken) obj;

        return Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "DECIMAL_TOKEN(" + value + ")";
    }
}
