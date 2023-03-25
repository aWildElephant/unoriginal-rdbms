package fr.awildelephant.rdbms.data.value;

public record TextValue(String value) implements DomainValue {

    public static TextValue textValue(String value) {
        return new TextValue(value);
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public String toString() {
        return "TextValue[" + value + ']';
    }
}
