package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.storage.data.record.Record;

import java.util.Comparator;
import java.util.List;

public final class MultipleColumnsComparator implements RecordComparator {

    private final List<SingleColumnComparator> comparators;

    public MultipleColumnsComparator(List<SingleColumnComparator> comparators) {
        this.comparators = comparators;
    }

    public int compare(Record record, Record reference) {
        for (Comparator<Record> comparator : comparators) {
            final int comparison = comparator.compare(record, reference);

            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }
}
