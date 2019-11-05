package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class NotEqualExpression implements ValueExpression {

    private final ValueExpression left;
    private final ValueExpression right;

    private NotEqualExpression(ValueExpression left, ValueExpression right) {
        this.left = left;
        this.right = right;
    }

    public static NotEqualExpression notEqualExpression(ValueExpression left, ValueExpression right) {
        return new NotEqualExpression(left, right);
    }

    public ValueExpression left() {
        return left;
    }

    public ValueExpression right() {
        return right;
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(left.variables(), right.variables());
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
        if (!(obj instanceof NotEqualExpression)) {
            return false;
        }

        final NotEqualExpression other = (NotEqualExpression) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
