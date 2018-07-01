package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.util.Collections.singletonList;

public class CollectionTable implements Table {

    private final Schema schema;
    private final Collection<Record> records;
    private final Map<String, UniqueIndex> indexes;

    CollectionTable(Schema schema, Collection<Record> records) {
        this.schema = schema;
        this.records = records;
        this.indexes = new HashMap<>();
    }

    @Override
    public Schema schema() {
        return schema;
    }

    @Override
    public void add(Record record) {
        indexes.forEach((columnName, index) -> {
            final int columnIndex = schema.column(columnName).index();

            final DomainValue value = record.get(columnIndex);

            if (!value.isNull()) {
                index.add(singletonList(value));
            }
        });
        records.add(record);
    }

    @Override
    public int numberOfTuples() {
        return records.size();
    }

    @Override
    public void createIndexOn(String column) {
        indexes.put(column, new UniqueIndex());
    }

    @Override
    public UniqueIndex indexOn(String column) {
        return indexes.get(column);
    }

    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }
}
