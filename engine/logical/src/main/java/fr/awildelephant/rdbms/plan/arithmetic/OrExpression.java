package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class OrExpression extends BinaryExpression {

    private OrExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static OrExpression orExpression(ValueExpression left, ValueExpression right) {
        return new OrExpression(left, right);
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new OrExpression(transformer.apply(left), transformer.apply(right));
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
        if (!(obj instanceof OrExpression)) {
            return false;
        }

        final OrExpression other = (OrExpression) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
