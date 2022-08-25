package fr.awildelephant.rdbms.driver;

import fr.awildelephant.rdbms.jdbc.abstraction.Value;
import fr.awildelephant.rdbms.rpc.generated.Rdbms;

import java.math.BigDecimal;
import java.sql.Date;

public class ValueProxy implements Value {

    private Rdbms.Value value;

    @Override
    public boolean isNull() {
        return value.hasNullValue();
    }

    @Override
    public boolean getBoolean() {
        return value.getBooleanValue().getValue();
    }

    @Override
    public BigDecimal getBigDecimal() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public int getInt() {
        return value.getIntegerValue().getValue();
    }

    @Override
    public long getLong() {
        return value.getLongValue().getValue();
    }

    @Override
    public String getString() {
        return value.getTextValue().getValue();
    }

    void setValue(Rdbms.Value value) {
        this.value = value;
    }
}
