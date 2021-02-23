package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public abstract class BinaryExpression implements ValueExpression {

    final ValueExpression left;
    final ValueExpression right;

    BinaryExpression(ValueExpression left, ValueExpression right) {
        this.left = left;
        this.right = right;
    }

    public ValueExpression left() {
        return left;
    }

    public ValueExpression right() {
        return right;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(left.variables(), right.variables());
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return accumulator.apply(function.apply(left), function.apply(right));
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", left)
                .append("right", right)
                .toString();
    }
}
