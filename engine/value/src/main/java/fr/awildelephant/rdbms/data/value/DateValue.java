package fr.awildelephant.rdbms.data.value;

import java.time.LocalDate;

public class DateValue extends AbstractValue {

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
}
