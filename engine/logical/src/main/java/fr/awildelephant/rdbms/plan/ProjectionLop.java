package fr.awildelephant.rdbms.plan;

import java.util.List;

public class ProjectionLop extends AbstractLop {

    private final LogicalOperator input;

    public ProjectionLop(List<String> outputColumns, LogicalOperator input) {
        super(input.schema().project(outputColumns));
        this.input = input;
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
