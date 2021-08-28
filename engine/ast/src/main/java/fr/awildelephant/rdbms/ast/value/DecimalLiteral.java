package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.math.BigDecimal;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

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
    public String toString() {
        return toStringBuilder(this)
                .append(value)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final DecimalLiteral other)) {
            return false;
        }

        return Objects.equals(value, other.value);
    }
}
