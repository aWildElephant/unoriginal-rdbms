package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.input.Values;

import java.util.function.Supplier;

public final class ValuesHolder implements Values {

    private Values values;

    public void setValues(Values values) {
        this.values = values;
    }

    @Override
    public DomainValue valueOf(int index) {
        return values.valueOf(index);
    }

    public Supplier<DomainValue> createSupplier(int index) {
        return () -> values.valueOf(index);
    }
}
