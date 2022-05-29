package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.tree.BinaryNode;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public abstract class BinaryExpression extends BinaryNode<ValueExpression, ValueExpression, ValueExpression> implements ValueExpression {


    BinaryExpression(ValueExpression left, ValueExpression right) {
        super(left, right);
    }

    public ValueExpression left() {
        return leftChild();
    }

    public ValueExpression right() {
        return rightChild();
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(leftChild().variables(), rightChild().variables());
    }

    @Override
    public <T> T reduce(Function<ValueExpression, T> function, BinaryOperator<T> accumulator) {
        return accumulator.apply(function.apply(leftChild()), function.apply(rightChild()));
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("leftChild", leftChild())
                .append("rightChild", rightChild())
                .toString();
    }
}
