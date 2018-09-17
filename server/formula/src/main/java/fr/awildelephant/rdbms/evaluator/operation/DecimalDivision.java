package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import java.math.BigDecimal;
import java.math.MathContext;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static java.math.BigDecimal.ZERO;

public class DecimalDivision implements Operation {

    private final Operation left;
    private final Operation right;

    private DecimalDivision(Operation left, Operation right) {
        this.left = left;
        this.right = right;
    }

    public static DecimalDivision decimalDivision(final Operation left, final Operation right) {
        return new DecimalDivision(left, right);
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
            throw new IllegalStateException("Division by zero");
        }

        return decimalValue(leftBigDecimalValue.divide(rightBigDecimalValue, MathContext.UNLIMITED));
    }

    @Override
    public Domain domain() {
        return DECIMAL;
    }

    @Override
    public boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }

    private boolean isZero(BigDecimal value) {
        return value.compareTo(ZERO) == 0;
    }
}
