package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

import java.math.BigDecimal;

public final class DecimalSubtraction extends BinaryOperation<DecimalOperation, DecimalOperation> implements DecimalOperation {

    public DecimalSubtraction(final DecimalOperation left, final DecimalOperation right) {
        super(left, right);
    }

    @Override
    public BigDecimal evaluateBigDecimal() {
        final BigDecimal leftValue = leftChild().evaluateBigDecimal();
        if (leftValue == null) {
            return null;
        }

        final BigDecimal rightValue = rightChild().evaluateBigDecimal();
        if (rightValue == null) {
            return null;
        }

        return leftValue.subtract(rightValue);
    }
}
