package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.exception.DivisionByZeroError;
import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

public final class IntegerDivision extends BinaryOperation<IntegerOperation, IntegerOperation> implements IntegerOperation {

    public IntegerDivision(final IntegerOperation left, final IntegerOperation right) {
        super(left, right);
    }

    @Override
    public Integer evaluateInteger() {
        final Integer leftValue = leftChild().evaluateInteger();
        if (leftValue == null || leftValue == 0) {
            return leftValue;
        }

        final Integer rightValue = rightChild().evaluateInteger();
        if (rightValue == null) {
            return null;
        }

        if (rightValue == 0) {
            throw new DivisionByZeroError();
        }

        return leftValue / rightValue;
    }
}
