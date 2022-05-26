package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.WriteableColumn;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;

public final class NoColumnTable implements WriteableTable {

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
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public List<WriteableColumn> columns() {
        return List.of();
    }

    @Override
    @NotNull
    public Iterator<Record> iterator() {
        return new NoColumnIterator(size);
    }
}
