package fr.awildelephant.rdbms.engine.domain;

public class IntegerValue implements DomainValue {

    private final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
