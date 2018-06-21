package fr.awildelephant.rdbms.plan;

import java.util.List;

public class Projection extends AbstractPlan {

    private final Plan input;

    public Projection(List<String> outputColumns, Plan input) {
        super(input.schema().project(outputColumns));
        this.input = input;
    }

    public Plan input() {
        return input;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
