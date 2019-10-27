package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.stream.Stream;

public abstract class BinaryExpression implements ValueExpression {

    final ValueExpression left;
    final ValueExpression right;

    BinaryExpression(ValueExpression left, ValueExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Stream<ColumnReference> variables() {
        return Stream.concat(left.variables(), right.variables());
    }

    public ValueExpression left() {
        return left;
    }

    public ValueExpression right() {
        return right;
    }
}
