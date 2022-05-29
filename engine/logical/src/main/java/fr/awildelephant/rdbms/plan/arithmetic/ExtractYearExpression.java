package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

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
        return INTEGER;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return child().variables();
    }

    @Override
    public ValueExpression transformInputs(Function<ValueExpression, ValueExpression> transformer) {
        return new ExtractYearExpression(transformer.apply(child()));
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return function.apply(child());
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
