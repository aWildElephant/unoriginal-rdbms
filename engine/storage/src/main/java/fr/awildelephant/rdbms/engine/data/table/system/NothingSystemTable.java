package fr.awildelephant.rdbms.engine.data.table.system;

import fr.awildelephant.rdbms.engine.constraint.ConstraintChecker;
import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Collections.emptyList;

public class NothingSystemTable implements ManagedTable {

    public static final Schema EMPTY_SCHEMA = new Schema(emptyList());

    @Override
    public UniqueIndex createIndexOn(List<String> columnNames) {
        throw readOnly();
    }

    @Override
    public void addChecker(ConstraintChecker checker) {
        throw readOnly();
    }

    @Override
    public Schema schema() {
        return EMPTY_SCHEMA;
    }

    @Override
    public void add(Record record) {
        throw readOnly();
    }

    @Override
    public int numberOfTuples() {
        return 0;
    }

    @Override
    public Iterator<Record> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Record next() {
                throw new NoSuchElementException();
            }
        };
    }

    private static RuntimeException readOnly() {
        return new UnsupportedOperationException("Nothing system table is readonly");
    }
}
