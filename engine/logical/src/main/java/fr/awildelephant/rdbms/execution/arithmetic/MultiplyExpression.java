package fr.awildelephant.rdbms.execution.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

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
        return new MultiplyExpression(transformer.apply(leftChild()), transformer.apply(rightChild()), domain);
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
        if (!(obj instanceof final MultiplyExpression other)) {
            return false;
        }

        return domain == other.domain
                && equalsBinaryNode(other);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", leftChild())
                .append("right", rightChild())
                .append("domain", domain)
                .toString();

    }
}
