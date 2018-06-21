package fr.awildelephant.rdbms.engine.data.value;

public class StringValue implements DomainValue {

    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
