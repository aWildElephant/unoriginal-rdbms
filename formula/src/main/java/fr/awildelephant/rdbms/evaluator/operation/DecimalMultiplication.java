package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static java.math.BigDecimal.ZERO;

public class DecimalMultiplication implements Operation {

    private final Operation left;
    private final Operation right;

    private DecimalMultiplication(Operation left, Operation right) {
        this.left = left;
        this.right = right;
    }

    public static DecimalMultiplication decimalMultiplication(final Operation left, final Operation right) {
        return new DecimalMultiplication(left, right);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue leftValue = left.evaluate();
        if (leftValue.isNull()) {
            return leftValue;
        }

        final BigDecimal leftBigDecimalValue = leftValue.getBigDecimal();
        if (isZero(leftBigDecimalValue)) {
            return leftValue;
        }

        final DomainValue rightValue = right.evaluate();
        if (rightValue.isNull()) {
            return nullValue();
        }

        final BigDecimal rightBigDecimalValue = rightValue.getBigDecimal();
        if (isZero(rightBigDecimalValue)) {
            return rightValue;
        }

        return decimalValue(leftBigDecimalValue.multiply(rightBigDecimalValue));
    }

    @Override
    public Domain domain() {
        return DECIMAL;
    }

    // TODO: also constant if left/right is constant & null or 0
    @Override
    public boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }
    private boolean isZero(BigDecimal value) {
        return value.compareTo(ZERO) == 0;
    }
}
