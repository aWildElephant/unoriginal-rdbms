package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ExtractYearExpression extends UnaryNode<ValueExpression, ValueExpression>
        implements ValueExpression {

    private ExtractYearExpression(ValueExpression child) {
        super(child);
    }

    public static ExtractYearExpression extractYearExpression(ValueExpression child) {
        return new ExtractYearExpression(child);
    }

    @Override
    public Domain domain() {
        return Domain.INTEGER;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new ExtractYearExpression(transformer.apply(child()));
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(child())
                .toString();
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ExtractYearExpression other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
