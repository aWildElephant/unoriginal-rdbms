package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.MultipleColumnsIterator;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ColumnBasedTable implements Table {

    private final Schema schema;
    private final List<Column> columns;

    public ColumnBasedTable(Schema schema, List<Column> columns) {
        this.schema = schema;
        this.columns = columns;
    }

    @Override
    public Schema schema() {
        return schema;
    }

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
    public Record get(int rowIndex) {
        final DomainValue[] values = new DomainValue[columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            values[i] = columns.get(i).get(rowIndex);
        }

        return new Record(values);
    }

    @Override
    public Iterator<Record> iterator() {
        return new MultipleColumnsIterator(columns);
    }

    @Override
    public List<Column> columns() {
        return columns;
    }
}
