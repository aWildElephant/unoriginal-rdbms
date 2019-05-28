package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.Iterator;

public class CollectionTable implements Table {

    private final Schema schema;
    private final Collection<Record> records;

    public CollectionTable(Schema schema, Collection<Record> records) {
        this.schema = schema;
        this.records = records;
    }

    @Override
    public Schema schema() {
        return schema;
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
