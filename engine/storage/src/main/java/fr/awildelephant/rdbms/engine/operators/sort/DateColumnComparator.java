package fr.awildelephant.rdbms.engine.operators.sort;

import fr.awildelephant.rdbms.engine.data.record.Record;

public class DateColumnComparator implements RecordComparator {

    private final int index;

    DateColumnComparator(int index) {
        this.index = index;
    }

    @Override
    public int compare(Record record, Record reference) {
        return record.get(index).getLocalDate().compareTo(reference.get(index).getLocalDate());
    }
}
