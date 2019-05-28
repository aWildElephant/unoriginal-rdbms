package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.ast.ColumnName;

import java.util.List;

public class SortLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ColumnName> columns;

    public SortLop(LogicalOperator input, List<ColumnName> columns) {
        super(input.schema());
        this.input = input;
        this.columns = columns;
    }

    public LogicalOperator input() {
        return input;
    }

    public List<ColumnName> columns() {
        return columns;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
