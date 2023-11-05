package fr.awildelephant.rdbms.operator.logical;

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
