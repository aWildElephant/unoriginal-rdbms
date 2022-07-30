package fr.awildelephant.rdbms.execution.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.function.Function;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class LessOrEqualExpression extends BinaryExpression {

    private LessOrEqualExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static LessOrEqualExpression lessOrEqualExpression(ValueExpression left, ValueExpression right) {
        return new LessOrEqualExpression(left, right);
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new LessOrEqualExpression(transformer.apply(leftChild()), transformer.apply(rightChild()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LessOrEqualExpression other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
