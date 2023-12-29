package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.Column;
import fr.awildelephant.rdbms.storage.data.record.Record;

import java.util.List;

public interface Table extends Iterable<Record> {

    Schema schema();

    int numberOfTuples();

    boolean isEmpty();

    List<? extends Column> columns();
}
