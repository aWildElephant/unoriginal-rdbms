package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;
import java.util.Objects;

public class DecimalValue extends AbstractValue {

    private final BigDecimal value;

    public DecimalValue(BigDecimal value) {
        this.value = value;
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
