package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class GreaterExpression extends BinaryExpression {

    private GreaterExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static GreaterExpression greaterExpression(ValueExpression left, ValueExpression right) {
        return new GreaterExpression(left, right);
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
        if (!(obj instanceof GreaterExpression)) {
            return false;
        }

        final GreaterExpression other = (GreaterExpression) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
