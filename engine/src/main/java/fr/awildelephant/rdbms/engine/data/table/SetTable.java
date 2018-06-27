package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.tuple.Tuple;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetTable extends AbstractTable {

    private final Set<Tuple> tuples;

    public SetTable(Schema schema) {
        super(schema);

        tuples = new HashSet<>();
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
