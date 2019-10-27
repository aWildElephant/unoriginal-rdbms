package fr.awildelephant.rdbms.engine.operators.values;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.Values;
import fr.awildelephant.rdbms.schema.ColumnReference;

public final class NoValues implements Values {

    private static final NoValues NO_VALUES = new NoValues();

    private NoValues() {

    }

    public static NoValues noValues() {
        return NO_VALUES;
    }

    @Override
    public DomainValue valueOf(ColumnReference column) {
        return null;
    }
}
