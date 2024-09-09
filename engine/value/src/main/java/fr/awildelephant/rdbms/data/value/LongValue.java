package fr.awildelephant.rdbms.data.value;

import fr.awildelephant.rdbms.data.value.exception.ValueOutOfBoundsError;

import java.math.BigDecimal;

public record LongValue(long value) implements DomainValue {

    public static LongValue longValue(long value) {
        return new LongValue(value);
    }

    @Override
    public int getInt() {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new ValueOutOfBoundsError(value);
        }

        return (int) value;
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
