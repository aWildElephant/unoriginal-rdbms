package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class CastExpression extends UnaryNode<ValueExpression, ValueExpression> implements ValueExpression {

    private final Domain domain;

    private CastExpression(ValueExpression child, Domain domain) {
        super(child);
        this.domain = domain;
    }

    public static CastExpression castExpression(ValueExpression child, Domain domain) {
        return new CastExpression(child, domain);
    }

    @Override
    public Domain domain() {
        return domain;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new CastExpression(transformer.apply(child()), domain);
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, child());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CastExpression other)) {
            return false;
        }

        return domain == other.domain
                && equalsUnaryNode(other);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("child", child())
                .append("castTo", domain)
                .toString();
    }
}
