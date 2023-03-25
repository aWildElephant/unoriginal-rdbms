package fr.awildelephant.rdbms.execution.operator.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.LongValue.longValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public class LongSumAccumulator implements Accumulator {

    private boolean foundNonNullValue;
    private long accumulator;

    @Override
    public void accumulate(DomainValue value) {
        if (!value.isNull()) {
            foundNonNullValue = true;
            accumulator += value.getLong();
        }
    }

    @Override
    public DomainValue result() {
        return foundNonNullValue ? longValue(accumulator) : nullValue();
    }
}
