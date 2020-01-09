package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public final class AnyAggregator implements Aggregator {

    private boolean found = false;

    @Override
    public boolean accumulate(DomainValue value) {
        if (!value.isNull() && value.getBool()) {
            found = true;
        }

        return found;
    }

    @Override
    public DomainValue aggregate() {
        return found ? trueValue() : falseValue();
    }
}
