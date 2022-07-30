package fr.awildelephant.rdbms.execution.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class AddExpression extends BinaryExpression {

    private final Domain domain;

    private AddExpression(ValueExpression left, ValueExpression right, Domain domain) {
        super(left, right);
        this.domain = domain;
    }

    public static AddExpression addExpression(ValueExpression left, ValueExpression right, Domain domain) {
        return new AddExpression(left, right, domain);
    }

    public Domain domain() {
        return domain;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new AddExpression(transformer.apply(leftChild()), transformer.apply(rightChild()), domain);
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("leftChild", leftChild())
                .append("rightChild", rightChild())
                .append("domain", domain)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, leftChild(), rightChild());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final AddExpression other)) {
            return false;
        }

        return domain == other.domain && equalsBinaryNode(other);
    }
}
