package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AliasedTable implements Table {

    private final Schema schema;
    private final Table wrappedTable;

    public AliasedTable(Schema schema, Table wrappedTable) {
        this.schema = schema;
        this.wrappedTable = wrappedTable;
    }

    @Override
    public Schema schema() {
        return schema;
    }

    @Override
    public void add(Record newRecord) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(Collection<Record> newRecords) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int numberOfTuples() {
        return wrappedTable.numberOfTuples();
    }

    @Override
    public boolean isEmpty() {
        return wrappedTable.isEmpty();
    }

    @Override
    public List<Column> columns() {
        return wrappedTable.columns();
    }

    @Override
    public Iterator<Record> iterator() {
        return wrappedTable.iterator();
    }
}
