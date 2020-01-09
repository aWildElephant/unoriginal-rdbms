package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class IntegerSumAggregator implements Aggregator {

    private boolean foundNonNullValue;
    private int accumulator;

    @Override
    public boolean accumulate(DomainValue value) {
        if (!value.isNull()) {
            foundNonNullValue = true;
            accumulator += value.getInt();
        }

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return foundNonNullValue ? integerValue(accumulator) : nullValue();
    }
}
