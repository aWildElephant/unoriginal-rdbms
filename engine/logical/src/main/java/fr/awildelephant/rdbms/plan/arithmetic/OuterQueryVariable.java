package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class OuterQueryVariable extends LeafNode<ValueExpression> implements ValueExpression {

    private final ColumnReference reference;
    private final Domain domain;

    private OuterQueryVariable(ColumnReference reference, Domain domain) {
        this.reference = reference;
        this.domain = domain;
    }

    public static OuterQueryVariable outerQueryVariable(ColumnReference reference, Domain domain) {
        return new OuterQueryVariable(reference, domain);
    }

    public ColumnReference reference() {
        return reference;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.of(reference);
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
        return Objects.hash(reference, domain);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final OuterQueryVariable other)) {
            return false;
        }

        return domain == other.domain
                && Objects.equals(reference, other.reference);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("name", reference)
                .append("domain", domain)
                .toString();
    }
}
