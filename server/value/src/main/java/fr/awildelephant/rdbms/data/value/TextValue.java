package fr.awildelephant.rdbms.data.value;

import java.util.Objects;

public final class TextValue extends AbstractValue {

    private final String value;

    private TextValue(String value) {
        this.value = value;
    }

    public static TextValue textValue(String value) {
        return new TextValue(value);
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
        if (!(obj instanceof TextValue)) {
            return false;
        }

        final TextValue other = (TextValue) obj;

        return Objects.equals(value, other.value);
    }
}
