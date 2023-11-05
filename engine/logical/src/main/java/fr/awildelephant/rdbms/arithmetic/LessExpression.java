package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.function.Function;

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
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new LessExpression(transformer.apply(leftChild()), transformer.apply(rightChild()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LessExpression other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
