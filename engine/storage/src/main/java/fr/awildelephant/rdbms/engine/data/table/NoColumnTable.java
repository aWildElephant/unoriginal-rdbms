package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;

public final class NoColumnTable implements Table {

    private int size;

    public NoColumnTable(int size) {
        this.size = size;
    }

    @Override
    public Schema schema() {
        return EMPTY_SCHEMA;
    }

    @Override
    public void add(Record newRecord) {
        size++;
    }

    @Override
    public void addAll(Collection<Record> newRecords) {
        size += newRecords.size();
    }

    @Override
    public int numberOfTuples() {
        return size;
    }

    @Override
    public Record get(int rowIndex) {
        return Record.EMPTY_RECORD;
    }

    @Override
    public List<Column> columns() {
        return List.of();
    }

    @Override
    public Iterator<Record> iterator() {
        return new NoColumnIterator(size);
    }
}
