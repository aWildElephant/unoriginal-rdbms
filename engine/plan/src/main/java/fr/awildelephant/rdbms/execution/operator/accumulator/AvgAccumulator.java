package fr.awildelephant.rdbms.execution.operator.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.math.BigDecimal;
import java.math.MathContext;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class AvgAccumulator implements Accumulator {

    private BigDecimal accumulator;
    private int numberOfNotNullValues;

    @Override
    public void accumulate(DomainValue value) {
        if (!value.isNull()) {
            numberOfNotNullValues++;

            final BigDecimal newValue = value.getBigDecimal();
            if (accumulator == null) {
                accumulator = newValue;
            } else {
                accumulator = accumulator.add(newValue);
            }
        }
    }

    @Override
    public DomainValue result() {
        return accumulator != null
                ? decimalValue(accumulator.divide(BigDecimal.valueOf(numberOfNotNullValues), MathContext.DECIMAL64))
                : nullValue();
    }
}
