package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public class BaseTableLop extends AbstractLop {

    private final String name;

    public BaseTableLop(String name, Schema schema) {
        super(schema);

        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
