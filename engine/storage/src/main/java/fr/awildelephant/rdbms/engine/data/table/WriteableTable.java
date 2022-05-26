package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.WriteableColumn;
import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.Collection;
import java.util.List;

public interface WriteableTable extends Table {

    void add(Record newRecord);

    void addAll(Collection<Record> newRecords);

    @Override
    List<WriteableColumn> columns();
}
