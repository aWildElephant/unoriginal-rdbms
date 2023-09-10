package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.storage.data.record.Record;

public class TextColumnComparator implements RecordComparator {
    private final int index;

    public TextColumnComparator(int index) {
        this.index = index;
    }

    @Override
    public int compare(Record record, Record reference) {
        final String left = record.get(index).getString();
        final String right = reference.get(index).getString();

        return left.compareTo(right);
    }
}
