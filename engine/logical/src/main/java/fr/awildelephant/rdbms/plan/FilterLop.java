package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

public class FilterLop extends AbstractLop {

    private final LogicalOperator input;
    private final ValueExpression filter;

    public FilterLop(LogicalOperator input, ValueExpression filter) {
        super(input.schema());
        this.input = input;
        this.filter = filter;
    }

    public LogicalOperator input() {
        return input;
    }

    public ValueExpression filter() {
        return filter;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
