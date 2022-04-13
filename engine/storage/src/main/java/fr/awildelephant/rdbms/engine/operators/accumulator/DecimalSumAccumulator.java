package fr.awildelephant.rdbms.engine.operators.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class DecimalSumAccumulator implements Accumulator {

    private BigDecimal accumulator;

    @Override
    public void accumulate(DomainValue value) {
        if (!value.isNull()) {
            if (accumulator != null) {
                accumulator = accumulator.add(value.getBigDecimal());
            } else {
                accumulator = value.getBigDecimal();
            }
        }
    }

    @Override
    public DomainValue result() {
        return accumulator != null ? decimalValue(accumulator) : nullValue();
    }
}
