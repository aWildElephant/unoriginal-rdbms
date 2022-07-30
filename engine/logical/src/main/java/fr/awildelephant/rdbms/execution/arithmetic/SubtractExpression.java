package fr.awildelephant.rdbms.execution.arithmetic;

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
        return new SubtractExpression(transformer.apply(leftChild()), transformer.apply(rightChild()), domain);
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, leftChild(), rightChild());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final SubtractExpression other)) {
            return false;
        }

        return domain == other.domain && equalsBinaryNode(other);
    }
}
