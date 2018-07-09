package fr.awildelephant.rdbms.engine.data.index;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.value.DomainValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueIndex {

    private final List<Integer> columnIndexes;
    private final Set<Record> index;

    public UniqueIndex(List<Integer> columnIndexes) {
        this.columnIndexes = columnIndexes;
        this.index = new HashSet<>();
    }

    public void register(Record record) {
        final Record projectedRecord = project(record);

        if (!projectedRecord.anyNull()) {
            index.add(projectedRecord);
        }
    }

    public boolean conflictsWith(Record record) {
        return index.contains(project(record));
    }

    private Record project(Record record) {
        final DomainValue[] values = new DomainValue[columnIndexes.size()];

        int i = 0;
        for (Integer columnIndex : columnIndexes) {
            values[i] = record.get(columnIndex);

            i++;
        }

        return new Record(values);
    }
}
