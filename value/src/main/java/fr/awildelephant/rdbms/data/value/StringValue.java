package fr.awildelephant.rdbms.data.value;

import java.util.Objects;

public final class StringValue extends AbstractValue {

    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StringValue)) {
            return false;
        }

        final StringValue other = (StringValue) obj;

        return Objects.equals(value, other.value);
    }
}
