package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.Value;

import java.math.BigDecimal;
import java.sql.Date;

public final class MockStringValue implements Value {

    private final String value;

    public MockStringValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isNull() {
        return value == null;
    }

    @Override
    public boolean getBoolean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigDecimal getBigDecimal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDate() {
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
    public String getString() {
        return value;
    }
}
