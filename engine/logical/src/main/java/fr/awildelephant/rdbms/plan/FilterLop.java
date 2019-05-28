package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.evaluator.Formula;

public class FilterLop extends AbstractLop {

    private final LogicalOperator input;
    private final Formula filter;

    public FilterLop(LogicalOperator input, Formula filter) {
        super(input.schema());
        this.input = input;
        this.filter = filter;
    }

    public LogicalOperator input() {
        return input;
    }

    public Formula filter() {
        return filter;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
