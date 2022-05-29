package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.tree.BinaryNode;

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
    public String toString() {
        return toStringBuilder(this)
                .append("leftChild", leftChild())
                .append("rightChild", rightChild())
                .toString();
    }
}
