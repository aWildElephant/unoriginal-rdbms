package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.engine.data.record.Record;

public class IntegerColumnComparator implements RecordComparator {

    private final int index;

    public IntegerColumnComparator(int index) {
        this.index = index;
    }

    @Override
    public int compare(Record record, Record reference) {
        return record.get(index).getInt() - reference.get(index).getInt();
    }
}
