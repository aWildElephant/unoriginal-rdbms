package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.ast.SortSpecification;

import java.util.List;

public final class SortLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<SortSpecification> columns;

    public SortLop(LogicalOperator input, List<SortSpecification> sortSpecificationList) {
        super(input.schema());
        this.input = input;
        this.columns = sortSpecificationList;
    }

    public LogicalOperator input() {
        return input;
    }

    public List<SortSpecification> sortSpecificationList() {
        return columns;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
