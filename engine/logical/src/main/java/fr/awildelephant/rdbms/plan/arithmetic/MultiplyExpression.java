package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.Function;

public final class MultiplyExpression extends BinaryExpression {

    private final Domain domain;

    private MultiplyExpression(ValueExpression left, ValueExpression right, Domain domain) {
        super(left, right);
        this.domain = domain;
    }

    public static MultiplyExpression multiplyExpression(ValueExpression left, ValueExpression right, Domain domain) {
        return new MultiplyExpression(left, right, domain);
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new MultiplyExpression(transformer.apply(left), transformer.apply(right), domain);
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
        if (!(obj instanceof MultiplyExpression)) {
            return false;
        }

        final MultiplyExpression other = (MultiplyExpression) obj;

        return domain == other.domain
                && Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
