package fr.awildelephant.rdbms.execution.operator.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class CountAccumulator implements Accumulator {

    private int count;

    @Override
    public void accumulate(DomainValue value) {
        if (!value.isNull()) {
            count++;
        }
    }

    @Override
    public DomainValue result() {
        return integerValue(count);
    }
}
