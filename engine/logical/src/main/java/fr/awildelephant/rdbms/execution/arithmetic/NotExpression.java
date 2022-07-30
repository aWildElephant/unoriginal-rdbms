package fr.awildelephant.rdbms.execution.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class NotExpression extends UnaryNode<ValueExpression, ValueExpression>
        implements ValueExpression {

    private NotExpression(ValueExpression child) {
        super(child);
    }

    public static NotExpression notExpression(ValueExpression child) {
        return new NotExpression(child);
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new NotExpression(transformer.apply(child()));
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
        if (!(obj instanceof final NotExpression other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
