package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.exception.DivisionByZeroError;
import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.ZERO;

public final class DecimalDivision extends BinaryOperation<DecimalOperation, DecimalOperation> implements DecimalOperation {

    public DecimalDivision(final DecimalOperation left, final DecimalOperation right) {
        super(left, right);
    }

    @Override
    public BigDecimal evaluateBigDecimal() {
        final BigDecimal rightValue = rightChild().evaluateBigDecimal();
        if (rightValue == null) {
            return null;
        }

        if (isZero(rightValue)) {
            throw new DivisionByZeroError();
        }

        final BigDecimal leftValue = leftChild().evaluateBigDecimal();
        if (leftValue == null || isZero(leftValue)) {
            return leftValue;
        }

        return leftValue.divide(rightValue, 7, RoundingMode.HALF_UP);
    }

    private boolean isZero(BigDecimal value) {
        return value.compareTo(ZERO) == 0;
    }
}
