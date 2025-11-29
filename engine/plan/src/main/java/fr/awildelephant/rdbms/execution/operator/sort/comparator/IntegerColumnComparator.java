package fr.awildelephant.rdbms.execution.operator.sort.comparator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public final class IntegerColumnComparator implements DomainValueComparator {

    public IntegerColumnComparator() {
    }

    @Override
    public int compare(DomainValue value, DomainValue reference) {
        return Integer.compare(value.getInt(), reference.getInt());
    }
}
