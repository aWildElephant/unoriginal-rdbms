package fr.awildelephant.rdbms.plan.arithmetic;

import java.util.stream.Stream;

public abstract class BinaryExpression implements ValueExpression {

    final ValueExpression left;
    final ValueExpression right;

    BinaryExpression(ValueExpression left, ValueExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Stream<String> variables() {
        return Stream.concat(left.variables(), right.variables());
    }

    public ValueExpression left() {
        return left;
    }

    public ValueExpression right() {
        return right;
    }
}
