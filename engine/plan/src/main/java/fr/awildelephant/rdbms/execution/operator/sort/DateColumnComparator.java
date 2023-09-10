package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.storage.data.record.Record;

public class DateColumnComparator implements RecordComparator {

    private final int index;

    public DateColumnComparator(int index) {
        this.index = index;
    }

    @Override
    public int compare(Record record, Record reference) {
        return record.get(index).getLocalDate().compareTo(reference.get(index).getLocalDate());
    }
}
