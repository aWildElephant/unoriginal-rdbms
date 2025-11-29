package fr.awildelephant.rdbms.execution.operator.sort.comparator;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Comparator;

/**
 * Wraps a comparator for DomainValue where null is the smallest possible value.
 */
public class NullsMinComparator implements DomainValueComparator {

    private final Comparator<DomainValue> comparator;

    public NullsMinComparator(Comparator<DomainValue> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(DomainValue first, DomainValue second) {
        if (first.isNull()) {
            return -1;
        }

        if (second.isNull()) {
            return 1;
        }

        return comparator.compare(first, second);
    }
}
