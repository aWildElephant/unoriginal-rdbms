package fr.awildelephant.rdbms.plan;

import java.util.List;

public class ProjectionNode extends AbstractPlan {

    private final Plan input;

    public ProjectionNode(List<String> outputColumns, Plan input) {
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
