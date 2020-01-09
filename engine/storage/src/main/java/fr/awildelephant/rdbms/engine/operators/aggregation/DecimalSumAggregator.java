package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class DecimalSumAggregator implements Aggregator {

    private BigDecimal accumulator;

    @Override
    public boolean accumulate(DomainValue value) {
        if (!value.isNull()) {
            if (accumulator != null) {
                accumulator = accumulator.add(value.getBigDecimal());
            } else {
                accumulator = value.getBigDecimal();
            }
        }

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return accumulator != null ? decimalValue(accumulator) : nullValue();
    }
}
