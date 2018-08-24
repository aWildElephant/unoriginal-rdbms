package fr.awildelephant.rdbms.data.value;

import java.time.LocalDate;

public class DateValue extends AbstractValue {

    private final LocalDate value;

    public DateValue(LocalDate value) {
        this.value = value;
    }

    @Override
    public LocalDate getLocalDate() {
        return value;
    }
}
