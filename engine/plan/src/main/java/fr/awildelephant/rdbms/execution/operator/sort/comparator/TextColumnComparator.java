package fr.awildelephant.rdbms.execution.operator.sort.comparator;

import fr.awildelephant.rdbms.data.value.DomainValue;

public final class TextColumnComparator implements DomainValueComparator {

    public TextColumnComparator() {
    }

    @Override
    public int compare(DomainValue value, DomainValue reference) {
        return value.getString().compareTo(reference.getString());
    }
}
