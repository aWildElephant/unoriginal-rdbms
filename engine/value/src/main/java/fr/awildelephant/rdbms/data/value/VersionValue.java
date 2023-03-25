package fr.awildelephant.rdbms.data.value;

public record VersionValue(long value) implements DomainValue {

    public static VersionValue versionValue(long value) {
        return new VersionValue(value);
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public String toString() {
        return "VersionValue[" + value + ']';
    }
}
