package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;

public record IntegerValue(int value) implements DomainValue {

    public static IntegerValue integerValue(int value) {
        return new IntegerValue(value);
    }

    @Override
    public BigDecimal getBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerValue[" + value + ']';
    }
}
