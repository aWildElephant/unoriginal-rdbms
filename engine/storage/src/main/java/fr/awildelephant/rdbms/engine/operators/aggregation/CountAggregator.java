package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class CountAggregator implements Aggregator {

    private int count;

    @Override
    public boolean accumulate(DomainValue value) {
        if (!value.isNull()) {
            count++;
        }

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return integerValue(count);
    }
}
