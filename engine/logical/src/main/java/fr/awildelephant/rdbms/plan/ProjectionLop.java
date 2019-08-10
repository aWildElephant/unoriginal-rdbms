package fr.awildelephant.rdbms.plan;

import java.util.List;

public class ProjectionLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<String> outputColumns;

    public ProjectionLop(LogicalOperator input, List<String> outputColumns) {
        super(input.schema().project(outputColumns));
        this.input = input;
        this.outputColumns = outputColumns;
    }

    public LogicalOperator input() {
        return input;
    }

    public List<String> outputColumns() {
        return outputColumns;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
