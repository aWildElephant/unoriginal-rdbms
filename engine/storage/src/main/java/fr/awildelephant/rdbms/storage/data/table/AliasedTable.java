package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.Column;
import fr.awildelephant.rdbms.storage.data.record.Record;

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
    public int numberOfTuples() {
        return wrappedTable.numberOfTuples();
    }

    @Override
    public boolean isEmpty() {
        return wrappedTable.isEmpty();
    }

    @Override
    public List<? extends Column> columns() {
        return wrappedTable.columns();
    }

    @Override
    public Iterator<Record> iterator() {
        return wrappedTable.iterator();
    }
}
