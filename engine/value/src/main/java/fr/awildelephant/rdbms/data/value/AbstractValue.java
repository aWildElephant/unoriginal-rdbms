package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public abstract class AbstractValue implements DomainValue {

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean getBool() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getInt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLong() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigDecimal getBigDecimal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDate getLocalDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Period getPeriod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException();
    }
}
