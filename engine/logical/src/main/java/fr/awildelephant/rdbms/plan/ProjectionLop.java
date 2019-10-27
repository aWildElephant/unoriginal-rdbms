package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.List;

public class ProjectionLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ColumnReference> outputColumns;

    public ProjectionLop(LogicalOperator input, List<ColumnReference> outputColumns) {
        super(input.schema().project(outputColumns));
        this.input = input;
        this.outputColumns = outputColumns;
    }

    public LogicalOperator input() {
        return input;
    }

    public List<ColumnReference> outputColumns() {
        return outputColumns;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
