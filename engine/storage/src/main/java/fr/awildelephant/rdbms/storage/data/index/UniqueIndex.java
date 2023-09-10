package fr.awildelephant.rdbms.storage.data.index;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.data.record.Tuple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class UniqueIndex {

    private final List<Integer> columnIndexes;
    private final Set<Record> index;

    public UniqueIndex(List<Integer> columnIndexes) {
        this.columnIndexes = columnIndexes;
        this.index = new HashSet<>();
    }

    public void register(Record record) {
        final Record projectedRecord = project(record);

        if (!anyNull(projectedRecord)) {
            index.add(projectedRecord);
        }
    }

    private boolean anyNull(Record record) {
        final int size = record.size();

        for (int i = 0; i < size; i++) {
            if (record.get(i).isNull()) {
                return true;
            }
        }

        return false;
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

        return new Tuple(values);
    }
}
