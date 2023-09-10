package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

// TODO: implement getting column by ColumnReference
public interface Table extends Iterable<Record> {

    Schema schema();

    int numberOfTuples();

    boolean isEmpty();

    List<? extends Column> columns();
}
