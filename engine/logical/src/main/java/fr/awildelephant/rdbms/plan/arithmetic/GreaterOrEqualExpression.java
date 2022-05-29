package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.function.Function;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class GreaterOrEqualExpression extends BinaryExpression {

    private GreaterOrEqualExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static GreaterOrEqualExpression greaterOrEqualExpression(ValueExpression left, ValueExpression right) {
        return new GreaterOrEqualExpression(left, right);
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new GreaterOrEqualExpression(transformer.apply(leftChild()), transformer.apply(rightChild()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final GreaterOrEqualExpression other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
