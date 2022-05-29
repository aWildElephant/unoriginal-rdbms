package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ConstantExpression extends LeafNode<ValueExpression> implements ValueExpression {

    private final DomainValue value;
    private final Domain domain;

    private ConstantExpression(DomainValue value, Domain domain) {
        this.value = value;
        this.domain = domain;
    }

    public static ConstantExpression constantExpression(DomainValue value, Domain domain) {
        return new ConstantExpression(value, domain);
    }

    public DomainValue value() {
        return value;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.of();
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return this;
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return null;
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ConstantExpression other)) {
            return false;
        }

        return domain == other.domain && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("value", value)
                .append("domain", domain)
                .toString();
    }
}
