package fr.awildelephant.rdbms.data.value;

import java.time.LocalDate;

public record DateValue(LocalDate value) implements DomainValue {

    public static DateValue dateValue(LocalDate value) {
        return new DateValue(value);
    }

    @Override
    public LocalDate getLocalDate() {
        return value;
    }

    @Override
    public String toString() {
        return "DateValue[" + value + ']';
    }
}
