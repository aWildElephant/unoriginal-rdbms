package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.storage.data.column.AppendableColumn;
import fr.awildelephant.rdbms.storage.data.record.Record;

import java.util.Collection;
import java.util.List;

// FIXME: this interface should probably not exist, these methods can be kept only in WritableColumn
public interface WriteableTable extends Table {

    void add(Record newRecord);

    void addAll(Collection<Record> newRecords);

    void addAll(Table source);

    @Override
    List<AppendableColumn> columns();
}
