package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class LessExpression extends BinaryExpression {

    private LessExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static LessExpression lessExpression(ValueExpression left, ValueExpression right) {
        return new LessExpression(left, right);
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
        if (!(obj instanceof LessExpression)) {
            return false;
        }

        final LessExpression other = (LessExpression) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
