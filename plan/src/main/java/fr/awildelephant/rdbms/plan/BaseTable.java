package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public class BaseTable extends AbstractPlan {

    private final String name;

    public BaseTable(String name, Schema schema) {
        super(schema);

        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
