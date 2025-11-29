package fr.awildelephant.rdbms.execution.operator.sort.comparator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public class DecimalColumnComparator implements DomainValueComparator {

    public DecimalColumnComparator() {
    }

    @Override
    public int compare(DomainValue value, DomainValue reference) {
        return value.getBigDecimal().compareTo(reference.getBigDecimal());
    }
}
