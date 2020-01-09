package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Comparator;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class MinAggregator implements Aggregator {

    private final Comparator<DomainValue> comparator;

    private DomainValue min = nullValue();

    public MinAggregator(Comparator<DomainValue> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean accumulate(DomainValue value) {
        if (min.isNull() || comparator.compare(value, min) < 0) {
            min = value;
        }

        return false;
    }

    @Override
    public DomainValue aggregate() {
        return min;
    }
}
