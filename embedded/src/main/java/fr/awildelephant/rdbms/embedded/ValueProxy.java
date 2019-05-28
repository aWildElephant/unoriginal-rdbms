package fr.awildelephant.rdbms.embedded;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.jdbc.abstraction.Value;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZoneId;

public class ValueProxy implements Value {

    private DomainValue value;

    void setValue(DomainValue value) {
        this.value = value;
    }

    @Override
    public boolean isNull() {
        return value.isNull();
    }

    @Override
    public boolean getBoolean() {
        return value.getBool();
    }

    @Override
    public BigDecimal getBigDecimal() {
        return value.getBigDecimal();
    }

    @Override
    public Date getDate() {
        return new Date(value.getLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public int getInt() {
        return value.getInt();
    }

    @Override
    public String getString() {
        return value.getString();
    }
}
