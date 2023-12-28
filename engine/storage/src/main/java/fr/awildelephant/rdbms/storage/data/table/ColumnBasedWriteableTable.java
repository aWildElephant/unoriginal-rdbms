package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.AppendableColumn;
import fr.awildelephant.rdbms.storage.data.record.MultipleColumnsIterator;
import fr.awildelephant.rdbms.storage.data.record.Record;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A writeable column-based table.
 */
public record ColumnBasedWriteableTable(Schema schema, List<AppendableColumn> columns) implements WriteableTable {

    @Override
    public void add(Record newRecord) {
        addAll(Collections.singleton(newRecord));
    }

    @Override
    public void addAll(Collection<Record> newRecords) {
        addAll(newRecords.size(), newRecords);
    }

    @Override
    public void addAll(Table source) {
        addAll(source.numberOfTuples(), source);
    }

    private void addAll(int numberOfNewRecords, Iterable<Record> newRecords) {
        columns.forEach(column -> column.ensureCapacity(column.size() + numberOfNewRecords));

        for (Record record : newRecords) {
            final int recordSize = record.size();
            for (int columnIndex = 0; columnIndex < recordSize; columnIndex++) {
                final AppendableColumn column = columns.get(columnIndex);

                column.add(record.get(columnIndex));
            }
        }
    }

    @Override
    public int numberOfTuples() {
        return columns.getFirst().size();
    }

    @Override
    public boolean isEmpty() {
        return numberOfTuples() == 0;
    }

    @Override
    @NotNull
    public Iterator<Record> iterator() {
        return new MultipleColumnsIterator(columns);
    }

    public Schema schema() {
        return schema;
    }

    public List<AppendableColumn> columns() {
        return columns;
    }

    @Override
    public String toString() {
        return "ColumnBasedWriteableTable[" +
                "schema=" + schema + ", " +
                "columns=" + columns + ']';
    }
}
