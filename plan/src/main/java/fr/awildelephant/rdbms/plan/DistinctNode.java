package fr.awildelephant.rdbms.plan;

public class DistinctNode extends AbstractPlan {

    private final Plan input;

    public DistinctNode(Plan input) {
        super(input.schema());
        this.input = input;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Plan input() {
        return input;
    }
}
