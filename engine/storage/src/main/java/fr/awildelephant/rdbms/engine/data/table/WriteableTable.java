package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.AppendOnlyColumn;
import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.Collection;
import java.util.List;

// FIXME: this interface should probably not exist, these methods can be kept only in WritableColumn
public interface WriteableTable extends Table {

    void add(Record newRecord);

    void addAll(Collection<Record> newRecords);

    void addAll(Table source);

    @Override
    List<AppendOnlyColumn> columns();
}
