package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.constraint.CompositeChecker;
import fr.awildelephant.rdbms.engine.constraint.ConstraintChecker;
import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableWithChecker implements ManagedTable {

    private final Table protectedTable;
    private final Collection<UniqueIndex> indexes;
    private final CompositeChecker checkers;

    TableWithChecker(Table protectedTable) {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public int numberOfTuples() {
        return protectedTable.numberOfTuples();
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
