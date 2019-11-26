package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.Function;

public final class SubtractExpression extends BinaryExpression {

    private final Domain domain;

    private SubtractExpression(ValueExpression left, ValueExpression right, Domain domain) {
        super(left, right);
        this.domain = domain;
    }

    public static SubtractExpression subtractExpression(ValueExpression left, ValueExpression right, Domain domain) {
        return new SubtractExpression(left, right, domain);
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new SubtractExpression(transformer.apply(left), transformer.apply(right), domain);
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SubtractExpression)) {
            return false;
        }

        final SubtractExpression other = (SubtractExpression) obj;

        return domain == other.domain
                && Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
