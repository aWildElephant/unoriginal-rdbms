package fr.awildelephant.rdbms.execution.operator.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.HashSet;
import java.util.Set;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class CountDistinctAccumulator implements Accumulator {

    private final Set<DomainValue> distinctValues = new HashSet<>();

    @Override
    public void accumulate(DomainValue value) {
        if (!value.isNull()) {
            distinctValues.add(value);
        }
    }

    @Override
    public DomainValue result() {
        return integerValue(distinctValues.size());
    }
}
