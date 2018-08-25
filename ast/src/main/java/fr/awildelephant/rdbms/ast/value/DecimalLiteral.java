package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.math.BigDecimal;
import java.util.Objects;

public final class DecimalLiteral implements AST {

    private final BigDecimal value;

    private DecimalLiteral(BigDecimal value) {
        this.value = value;
    }

    public static DecimalLiteral decimalLiteral(BigDecimal value) {
        return new DecimalLiteral(value);
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DecimalLiteral)) {
            return false;
        }

        final DecimalLiteral other = (DecimalLiteral) obj;

        return Objects.equals(value, other.value);
    }
}
