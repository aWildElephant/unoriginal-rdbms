package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;

public record LongValue(long value) implements DomainValue {

    public static LongValue longValue(long value) {
        return new LongValue(value);
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return BigDecimal.valueOf(value);
    }

    @Override
    public String toString() {
        return "LongValue[" + value + ']';
    }
}
