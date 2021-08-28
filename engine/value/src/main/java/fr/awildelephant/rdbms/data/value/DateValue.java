package fr.awildelephant.rdbms.data.value;

import java.time.LocalDate;
import java.util.Objects;

public final class DateValue extends AbstractValue {

    private final LocalDate value;

    private DateValue(LocalDate value) {
        this.value = value;
    }

    public static DateValue dateValue(LocalDate value) {
        return new DateValue(value);
    }

    @Override
    public LocalDate getLocalDate() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final DateValue other)) {
            return false;
        }

        return Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "DateValue[" + value + ']';
    }
}
