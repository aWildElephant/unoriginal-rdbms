package fr.awildelephant.rdbms.execution.operator.sort.comparator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public class DateColumnComparator implements DomainValueComparator {

    public DateColumnComparator() {
    }

    @Override
    public int compare(DomainValue value, DomainValue reference) {
        return value.getLocalDate().compareTo(reference.getLocalDate());
    }
}
