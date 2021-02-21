package fr.awildelephant.rdbms.evaluator.input;

import fr.awildelephant.rdbms.data.value.DomainValue;

public final class NoValues implements Values {

    private static final NoValues NO_VALUES = new NoValues();

    private NoValues() {

    }

    public static NoValues noValues() {
        return NO_VALUES;
    }

    @Override
    public DomainValue valueOf(int index) {
        throw new IllegalStateException();
    }
}
