package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public final class DecimalMultiplication extends BinaryOperation<DecimalOperation, DecimalOperation> implements DecimalOperation {

    public DecimalMultiplication(final DecimalOperation left, final DecimalOperation right) {
        super(left, right);
    }

    @Override
    public BigDecimal evaluateBigDecimal() {
        final BigDecimal leftValue = leftChild().evaluateBigDecimal();
        if (leftValue == null || isZero(leftValue)) {
            return leftValue;
        }

        final BigDecimal rightValue = rightChild().evaluateBigDecimal();
        if (rightValue == null || isZero(rightValue)) {
            return rightValue;
        }

        return leftValue.multiply(rightValue);
    }

    private boolean isZero(BigDecimal value) {
        return value.compareTo(ZERO) == 0;
    }
}
