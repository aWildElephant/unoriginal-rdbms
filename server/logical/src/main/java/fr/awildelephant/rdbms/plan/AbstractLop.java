package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public abstract class AbstractLop implements LogicalOperator {

    private final Schema schema;

    AbstractLop(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Schema schema() {
        return schema;
    }
}
