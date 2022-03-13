package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.MultipleColumnsIterator;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public record ColumnBasedTable(Schema schema, List<Column> columns) implements Table {

    @Override
    public void add(Record newRecord) {
        addAll(Collections.singleton(newRecord));
    }

    @Override
    public void addAll(Collection<Record> newRecords) {
        final int numberOfColumns = schema.numberOfAttributes();

        final int numberOfNewRows = newRecords.size();

        columns.forEach(column -> column.ensureCapacity(column.size() + numberOfNewRows));

        for (Record record : newRecords) {
            for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
                final Column column = columns.get(columnIndex);

                column.add(record.get(columnIndex));
            }
        }
    }

    @Override
    public int numberOfTuples() {
        return columns.get(0).size();
    }

    @Override
    public boolean isEmpty() {
        return numberOfTuples() == 0;
    }

    @Override
    public Iterator<Record> iterator() {
        return new MultipleColumnsIterator(columns);
    }
}
