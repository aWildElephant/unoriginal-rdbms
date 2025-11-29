package fr.awildelephant.rdbms.execution.operator.sort;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.storage.data.record.Record;

import java.util.Comparator;

public final class SingleColumnComparator implements RecordComparator {

    private final int columnIndex;
    private final Comparator<DomainValue> valueComparator;

    public SingleColumnComparator(int columnIndex, Comparator<DomainValue> valueComparator) {
        this.columnIndex = columnIndex;
        this.valueComparator = valueComparator;
    }

    @Override
    public int compare(Record record, Record reference) {
        return valueComparator.compare(record.get(columnIndex), reference.get(columnIndex));
    }
}
