package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.constraint.CompositeChecker;
import fr.awildelephant.rdbms.storage.constraint.ConstraintChecker;
import fr.awildelephant.rdbms.storage.data.column.AppendableColumn;
import fr.awildelephant.rdbms.storage.data.index.UniqueIndex;
import fr.awildelephant.rdbms.storage.data.record.Record;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableWithChecker implements ManagedTable {

    private final WriteableTable protectedTable;
    private final Collection<UniqueIndex> indexes;
    private final CompositeChecker checkers;

    TableWithChecker(WriteableTable protectedTable) {
        this.protectedTable = protectedTable;
        this.checkers = new CompositeChecker();
        this.indexes = new LinkedList<>();
    }

    @Override
    public Schema schema() {
        return protectedTable.schema();
    }

    @Override
    public void add(Record newRecord) {
        checkers.check(newRecord);

        indexes.forEach(index -> index.register(newRecord));

        protectedTable.add(newRecord);
    }

    @Override
    public void addAll(Collection<Record> newRecords) {
        newRecords.forEach(this::add);
    }

    @Override
    public void addAll(Table source) {
        source.forEach(this::add);
    }

    @Override
    public int numberOfTuples() {
        return protectedTable.numberOfTuples();
    }

    @Override
    public boolean isEmpty() {
        return protectedTable.isEmpty();
    }

    @Override
    public List<AppendableColumn> columns() {
        return protectedTable.columns();
    }

    @Override
    public Iterator<Record> iterator() {
        return protectedTable.iterator();
    }

    @Override
    public UniqueIndex createIndexOn(List<String> columnNames) {
        final Schema schema = protectedTable.schema();
        final UniqueIndex index = new UniqueIndex(columnNames.stream().map(schema::indexOf).sorted().collect(toList()));

        indexes.add(index);

        return index;
    }

    @Override
    public void addChecker(ConstraintChecker checker) {
        checkers.add(checker);
    }
}
