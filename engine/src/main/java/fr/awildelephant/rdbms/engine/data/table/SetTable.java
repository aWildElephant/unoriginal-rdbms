package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetTable extends AbstractTable {

    private final Set<Record> records;

    public SetTable(Schema schema) {
        super(schema);

        records = new HashSet<>();
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
