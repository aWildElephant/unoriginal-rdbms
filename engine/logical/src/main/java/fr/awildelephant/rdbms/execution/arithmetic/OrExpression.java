package fr.awildelephant.rdbms.execution.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

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
        return new OrExpression(transformer.apply(leftChild()), transformer.apply(rightChild()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final OrExpression other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
