package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.List;

public interface Table extends Iterable<Record> {

    Schema schema();

    // TODO: move add and addAll to another interface
    void add(Record newRecord);

    void addAll(Collection<Record> newRecords);

    int numberOfTuples();

    boolean isEmpty();

    List<Column> columns();
}
