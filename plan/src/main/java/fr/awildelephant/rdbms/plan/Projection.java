package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public class Projection implements Plan {

    private final Plan input;

    public Projection(Plan input) {
        this.input = input;
    }

    public Plan input() {
        return input;
    }

    @Override
    public Schema schema() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
