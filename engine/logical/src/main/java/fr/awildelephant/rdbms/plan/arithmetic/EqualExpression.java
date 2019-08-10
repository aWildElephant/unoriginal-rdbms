package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class EqualExpression extends BinaryExpression {

    private EqualExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static EqualExpression equalExpression(ValueExpression left, ValueExpression right) {
        return new EqualExpression(left, right);
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EqualExpression)) {
            return false;
        }

        final EqualExpression other = (EqualExpression) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
