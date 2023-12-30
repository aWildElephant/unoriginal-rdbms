package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

public final class IntegerAddition extends BinaryOperation<IntegerOperation, IntegerOperation> implements IntegerOperation {

    public IntegerAddition(final IntegerOperation left, final IntegerOperation right) {
        super(left, right);
    }

    @Override
    public Integer evaluateInteger() {
        final Integer leftValue = leftChild().evaluateInteger();
        if (leftValue == null) {
            return null;
        }

        final Integer rightValue = rightChild().evaluateInteger();
        if (rightValue == null) {
            return null;
        }

        return leftValue + rightValue;
    }
}
