package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class IsNullExpression extends UnaryNode<ValueExpression, ValueExpression>
        implements ValueExpression {

    public IsNullExpression(ValueExpression child) {
        super(child);
    }

    public static IsNullExpression isNullExpression(ValueExpression child) {
        return new IsNullExpression(child);
    }

    @Override
    public Domain domain() {
        return Domain.BOOLEAN;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return isNullExpression(transformer.apply(child()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(child())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final IsNullExpression other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
