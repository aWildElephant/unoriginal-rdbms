package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.tuple.Tuple;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListTable extends AbstractTable {

    private final List<Tuple> tuples;

    public ListTable(Schema schema) {
        this(schema, 8);
    }

    public ListTable(Schema schema, int initialCapacity) {
        super(schema);
        this.tuples = new ArrayList<>(initialCapacity);
    }

    @Override
    public void add(Tuple tuple) {
        tuples.add(tuple);
    }

    @Override
    public int numberOfTuples() {
        return tuples.size();
    }

    @Override
    public Iterator<Tuple> iterator() {
        return tuples.iterator();
    }
}
