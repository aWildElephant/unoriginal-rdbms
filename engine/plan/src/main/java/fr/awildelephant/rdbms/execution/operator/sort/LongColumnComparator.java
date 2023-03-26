package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.engine.data.record.Record;

public final class LongColumnComparator implements RecordComparator {

    private final int index;

    public LongColumnComparator(int index) {
        this.index = index;
    }

    @Override
    public int compare(Record record, Record reference) {
        return Long.compare(record.get(index).getLong(), reference.get(index).getLong());
    }
}
