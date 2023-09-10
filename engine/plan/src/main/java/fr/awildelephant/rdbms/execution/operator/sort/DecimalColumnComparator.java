package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.storage.data.record.Record;

public class DecimalColumnComparator implements RecordComparator {

    private final int index;

    public DecimalColumnComparator(int index) {
        this.index = index;
    }

    @Override
    public int compare(Record record, Record reference) {
        return record.get(index).getBigDecimal().compareTo(reference.get(index).getBigDecimal());
    }
}
