package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;
import java.util.Objects;

public final class DecimalValue implements DomainValue {

    private final BigDecimal value;

    private DecimalValue(BigDecimal value) {
        this.value = value;
    }

    public static DecimalValue decimalValue(BigDecimal value) {
        return new DecimalValue(value);
    }

    @Override
    public BigDecimal getBigDecimal() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final DecimalValue other)) {
            return false;
        }

        return Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "DecimalValue[" + value + ']';
    }
}
