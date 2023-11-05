package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.TernaryNode;

import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class BetweenExpression extends TernaryNode<ValueExpression, ValueExpression, ValueExpression, ValueExpression>
        implements ValueExpression {

    private BetweenExpression(ValueExpression value, ValueExpression lowerBound, ValueExpression upperBound) {
        super(value, lowerBound, upperBound);
    }

    public static BetweenExpression betweenExpression(ValueExpression value, ValueExpression lowerBound, ValueExpression upperBound) {
        return new BetweenExpression(value, lowerBound, upperBound);
    }

    public ValueExpression value() {
        return firstChild();
    }

    public ValueExpression lowerBound() {
        return secondChild();
    }

    public ValueExpression upperBound() {
        return thirdChild();
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new BetweenExpression(transformer.apply(firstChild()),
                transformer.apply(secondChild()),
                transformer.apply(thirdChild()));
    }

    @Override
    public <T> T accept(ValueExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("value", firstChild())
                .append("lowerBound", secondChild())
                .append("upperBound", thirdChild())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final BetweenExpression other)) {
            return false;
        }

        return equalsTernary(other);
    }
}
