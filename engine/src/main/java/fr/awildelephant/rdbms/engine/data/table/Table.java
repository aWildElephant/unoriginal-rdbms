package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.tuple.Tuple;
import fr.awildelephant.rdbms.schema.Schema;

public interface Table extends Iterable<Tuple> {

    Schema schema();

    void add(Tuple tuple);

    int numberOfTuples();
}
