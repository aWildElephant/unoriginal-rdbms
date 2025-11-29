package fr.awildelephant.rdbms.execution.operator.sort.comparator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public final class LongColumnComparator implements DomainValueComparator {

    public LongColumnComparator() {
    }

    @Override
    public int compare(DomainValue value, DomainValue reference) {
        return Long.compare(value.getLong(), reference.getLong());
    }
}
