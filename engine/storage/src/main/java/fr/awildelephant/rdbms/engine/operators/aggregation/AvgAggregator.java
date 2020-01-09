package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.math.BigDecimal;
import java.math.MathContext;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class AvgAggregator implements Aggregator {

    private BigDecimal accumulator;
    private int numberOfNotNullValues;

    @Override
    public boolean accumulate(DomainValue value) {
        if (!value.isNull()) {
            numberOfNotNullValues++;

            final BigDecimal newValue = value.getBigDecimal();
            if (accumulator == null) {
                accumulator = newValue;
            } else {
                accumulator = accumulator.add(newValue);
            }
        }

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return accumulator != null
                ? decimalValue(accumulator.divide(BigDecimal.valueOf(numberOfNotNullValues), MathContext.DECIMAL64))
                : nullValue();
    }
}
