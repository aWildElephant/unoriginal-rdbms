package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;

public final class DecimalColumn extends ObjectColumn<BigDecimal> {

    public DecimalColumn(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    protected DomainValue transform(BigDecimal value) {
        return decimalValue(value);
    }

    @Override
    protected BigDecimal extract(DomainValue value) {
        return value.getBigDecimal();
    }
}
