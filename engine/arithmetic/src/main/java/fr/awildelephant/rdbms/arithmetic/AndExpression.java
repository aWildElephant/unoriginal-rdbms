package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.function.Function;

public final class AndExpression extends BinaryExpression {

    private AndExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public static AndExpression andExpression(ValueExpression left, ValueExpression right) {
        return new AndExpression(left, right);
    }

    @Override
    public Domain domain() {
        return Domain.BOOLEAN;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new AndExpression(transformer.apply(leftChild()), transformer.apply(rightChild()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final AndExpression other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
