package fr.awildelephant.rdbms.engine.operators.accumulator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public final class AnyAccumulator implements Accumulator {

    private DomainValue result = nullValue();

    @Override
    public void accumulate(DomainValue value) {
        if (!value.isNull()) {
            if (value.getBool()) {
                result = trueValue();
            } else if (result.isNull()) {
                result = falseValue();
            }
        }
    }

    @Override
    public DomainValue result() {
        return result;
    }
}
