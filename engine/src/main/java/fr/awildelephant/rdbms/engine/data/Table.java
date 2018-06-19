package fr.awildelephant.rdbms.engine.data;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;

public class Table extends ArrayList<Tuple> {

    private final Schema schema;

    public Table(Schema schema) {
        this(schema, 8);
    }

    public Table(Schema schema, int initialCapacity) {
        super(initialCapacity);
        this.schema = schema;
    }

    public Schema schema() {
        return schema;
    }
}
