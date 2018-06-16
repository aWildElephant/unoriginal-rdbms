package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public abstract class AbstractPlan implements Plan {

    private final Schema schema;

    AbstractPlan(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Schema schema() {
        return schema;
    }
}
