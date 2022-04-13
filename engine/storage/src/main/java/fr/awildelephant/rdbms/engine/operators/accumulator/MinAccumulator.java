package fr.awildelephant.rdbms.engine.operators.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Comparator;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class MinAccumulator implements Accumulator {

    private final Comparator<DomainValue> comparator;

    private DomainValue min = nullValue();

    public MinAccumulator(Comparator<DomainValue> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void accumulate(DomainValue value) {
        if (min.isNull() || comparator.compare(value, min) < 0) {
            min = value;
        }
    }

    @Override
    public DomainValue result() {
        return min;
    }
}
