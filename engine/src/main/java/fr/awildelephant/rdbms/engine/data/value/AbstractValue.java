package fr.awildelephant.rdbms.engine.data.value;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class AbstractValue implements DomainValue {

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int getInt() {
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
    public String getString() {
        throw new UnsupportedOperationException();
    }
}
