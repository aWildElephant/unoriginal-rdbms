package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

public final class IntegerMultiplication extends BinaryOperation<IntegerOperation, IntegerOperation> implements IntegerOperation {

    public IntegerMultiplication(final IntegerOperation left, final IntegerOperation right) {
        super(left, right);
    }

    @Override
    public Integer evaluateInteger() {
        final Integer leftValue = leftChild().evaluateInteger();
        if (leftValue == null || leftValue == 0) {
            return leftValue;
        }

        final Integer rightValue = rightChild().evaluateInteger();
        if (rightValue == null || rightValue == 0) {
            return rightValue;
        }

        return leftValue * rightValue;
    }
}
