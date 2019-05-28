package fr.awildelephant.rdbms.plan;

public class CollectLop extends AbstractLop {

    private final LogicalOperator input;

    public CollectLop(LogicalOperator input) {
        super(input.schema());

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
