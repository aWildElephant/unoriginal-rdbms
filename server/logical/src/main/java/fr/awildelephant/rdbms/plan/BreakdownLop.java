package fr.awildelephant.rdbms.plan;

import java.util.List;

public class BreakdownLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<String> breakdowns;

    public BreakdownLop(LogicalOperator input, List<String> breakdowns) {
        super(input.schema());

        this.input = input;
        this.breakdowns = breakdowns;
    }

    public List<String> breakdowns() {
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
