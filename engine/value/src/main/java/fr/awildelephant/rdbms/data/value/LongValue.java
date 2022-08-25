package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;

public final class LongValue extends AbstractValue {

    private final long value;

    private LongValue(long value) {
        this.value = value;
    }

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
    public int hashCode() {
        return (int) value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LongValue other)) {
            return false;
        }

        return value == other.value;
    }

    @Override
    public String toString() {
        return "LongValue[" + value + ']';
    }
}
