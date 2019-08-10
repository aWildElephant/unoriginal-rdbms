package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;
import java.util.Objects;

public final class DecimalValue extends AbstractValue {

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
        if (!(obj instanceof DecimalValue)) {
            return false;
        }

        final DecimalValue other = (DecimalValue) obj;

        return Objects.equals(value, other.value);
    }
}
