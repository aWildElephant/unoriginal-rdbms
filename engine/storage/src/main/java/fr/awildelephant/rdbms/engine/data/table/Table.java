package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;

public interface Table extends Iterable<Record> {

    Schema schema();

    void add(Record newRecord);

    void addAll(Collection<Record> newRecords);

    int numberOfTuples();
}
