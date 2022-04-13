package fr.awildelephant.rdbms.engine.operators.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class CountStarAccumulator implements Accumulator {

    private int accumulator;

    @Override
    public void accumulate(DomainValue unused) {
        accumulator++;
    }

    @Override
    public DomainValue result() {
        return integerValue(accumulator);
    }
}
