package fr.awildelephant.rdbms.plan;

public final class LimitLop extends AbstractLop {

    private final LogicalOperator input;
    private final int limit;

    public LimitLop(LogicalOperator input, int limit) {
        super(input.schema());

        this.input = input;
        this.limit = limit;
    }

    public LogicalOperator input() {
        return input;
    }

    public int limit() {
        return limit;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
