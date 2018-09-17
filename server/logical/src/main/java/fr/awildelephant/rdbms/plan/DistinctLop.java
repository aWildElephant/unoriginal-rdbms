package fr.awildelephant.rdbms.plan;

public class DistinctLop extends AbstractLop {

    private final LogicalOperator input;

    public DistinctLop(LogicalOperator input) {
        super(input.schema());
        this.input = input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public LogicalOperator input() {
        return input;
    }
}
