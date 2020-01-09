package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.HashSet;
import java.util.Set;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class CountDistinctAggregator implements Aggregator {

    private Set<DomainValue> distinctValues = new HashSet<>();

    @Override
    public boolean accumulate(DomainValue value) {
        if (!value.isNull()) {
            distinctValues.add(value);
        }

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return integerValue(distinctValues.size());
    }
}
