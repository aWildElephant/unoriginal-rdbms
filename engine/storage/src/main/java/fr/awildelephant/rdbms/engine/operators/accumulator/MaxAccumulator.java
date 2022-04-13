package fr.awildelephant.rdbms.engine.operators.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Comparator;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class MaxAccumulator implements Accumulator {

    private final Comparator<DomainValue> comparator;

    private DomainValue max = nullValue();

    public MaxAccumulator(Comparator<DomainValue> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void accumulate(DomainValue value) {
        if (max.isNull() || comparator.compare(value, max) > 0) {
            max = value;
        }
    }

    @Override
    public DomainValue result() {
        return max;
    }
}
