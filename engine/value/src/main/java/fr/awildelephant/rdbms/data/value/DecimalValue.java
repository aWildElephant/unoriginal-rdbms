package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;

public record DecimalValue(BigDecimal value) implements DomainValue {

    public static DecimalValue decimalValue(BigDecimal value) {
        return new DecimalValue(value);
    }

    @Override
    public BigDecimal getBigDecimal() {
        return value;
    }

    @Override
    public String toString() {
        return "DecimalValue[" + value + ']';
    }
}
