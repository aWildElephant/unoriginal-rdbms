package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.time.LocalDate;

import static fr.awildelephant.rdbms.data.value.DateValue.dateValue;

public final class DateColumn extends ObjectColumn<LocalDate> {

    public DateColumn(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    protected DomainValue transform(LocalDate value) {
        return dateValue(value);
    }

    @Override
    protected LocalDate extract(DomainValue value) {
        return value.getLocalDate();
    }
}
