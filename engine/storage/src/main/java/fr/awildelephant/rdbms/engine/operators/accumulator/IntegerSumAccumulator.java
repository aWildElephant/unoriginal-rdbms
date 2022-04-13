package fr.awildelephant.rdbms.engine.operators.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class IntegerSumAccumulator implements Accumulator {

    private boolean foundNonNullValue;
    private int accumulator;

    @Override
    public void accumulate(DomainValue value) {
        if (!value.isNull()) {
            foundNonNullValue = true;
            accumulator += value.getInt();
        }
    }

    @Override
    public DomainValue result() {
        return foundNonNullValue ? integerValue(accumulator) : nullValue();
    }
}
