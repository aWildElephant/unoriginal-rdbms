package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class CountStarAggregator implements Aggregator {

    private int accumulator;

    @Override
    public boolean accumulate(DomainValue unused) {
        accumulator++;

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return integerValue(accumulator);
    }
}
