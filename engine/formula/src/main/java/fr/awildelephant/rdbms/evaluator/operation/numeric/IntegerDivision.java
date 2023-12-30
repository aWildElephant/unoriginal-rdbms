package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

public final class IntegerDivision extends BinaryOperation<IntegerOperation, IntegerOperation> implements IntegerOperation {

    public IntegerDivision(final IntegerOperation left, final IntegerOperation right) {
        super(left, right);
    }

    @Override
    public Integer evaluate() {
        final Integer leftValue = leftChild().evaluate();
        if (leftValue == null || leftValue == 0) {
            return leftValue;
        }

        final Integer rightValue = rightChild().evaluate();
        if (rightValue == null) {
            return null;
        }

        if (rightValue == 0) {
            throw new IllegalStateException("Division by zero");
        }

        return leftValue / rightValue;
    }
}
