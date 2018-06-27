package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.schema.Schema;

public abstract class AbstractTable implements Table {

    private final Schema schema;

    AbstractTable(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Schema schema() {
        return schema;
    }
}
