package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;

import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;

public final class DivideExpression extends BinaryExpression {

    private DivideExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static DivideExpression divideExpression(ValueExpression left, ValueExpression right) {
        return new DivideExpression(left, right);
    }

    @Override
    public Domain domain() {
        return DECIMAL;
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
        if (!(obj instanceof DivideExpression)) {
            return false;
        }

        final DivideExpression other = (DivideExpression) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
