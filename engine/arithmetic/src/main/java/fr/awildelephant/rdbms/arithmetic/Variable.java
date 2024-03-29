package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Variable extends LeafNode<ValueExpression> implements ValueExpression {

    private final ColumnReference reference;
    private final Domain domain;

    private Variable(ColumnReference reference, Domain domain) {
        this.reference = reference;
        this.domain = domain;
    }

    public static Variable variable(ColumnReference reference, Domain domain) {
        return new Variable(reference, domain);
    }

    public ColumnReference reference() {
        return reference;
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return this;
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, reference);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Variable other)) {
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
