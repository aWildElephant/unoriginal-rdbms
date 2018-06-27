package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListTable extends AbstractTable {

    private final List<Record> records;

    public ListTable(Schema schema) {
        this(schema, 8);
    }

    public ListTable(Schema schema, int initialCapacity) {
        super(schema);
        this.records = new ArrayList<>(initialCapacity);
    }

    @Override
    public void add(Record record) {
        records.add(record);
    }

    @Override
    public int numberOfTuples() {
        return records.size();
    }

    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }
}
