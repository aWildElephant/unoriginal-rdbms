package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Comparator;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class MaxAggregator implements Aggregator {

    private final Comparator<DomainValue> comparator;

    private DomainValue max = nullValue();

    public MaxAggregator(Comparator<DomainValue> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean accumulate(DomainValue value) {
        if (max.isNull() || comparator.compare(value, max) > 0) {
            max = value;
        }

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return max;
    }
}
