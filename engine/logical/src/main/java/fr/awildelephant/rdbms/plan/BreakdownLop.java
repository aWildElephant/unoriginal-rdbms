package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.List;

public class BreakdownLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ColumnReference> breakdowns;

    public BreakdownLop(LogicalOperator input, List<ColumnReference> breakdowns) {
        super(input.schema());

        this.input = input;
        this.breakdowns = breakdowns;
    }

    public List<ColumnReference> breakdowns() {
        return breakdowns;
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
