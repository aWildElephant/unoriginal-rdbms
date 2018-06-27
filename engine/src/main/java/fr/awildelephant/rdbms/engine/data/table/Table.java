package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

public interface Table extends Iterable<Record> {

    Schema schema();

    void add(Record record);

    int numberOfTuples();
}
