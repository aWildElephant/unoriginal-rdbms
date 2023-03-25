package fr.awildelephant.rdbms.data.value;

import java.util.Objects;

public final class VersionValue extends AbstractValue {

    private final long value;

    private VersionValue(long value) {
        this.value = value;
    }

    public static VersionValue versionValue(long value) {
        return new VersionValue(value);
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final VersionValue other)) {
            return false;
        }

        return Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "VersionValue[" + value + ']';
    }
}
