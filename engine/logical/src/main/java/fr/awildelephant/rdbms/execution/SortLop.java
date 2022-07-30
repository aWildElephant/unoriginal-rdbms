package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.execution.sort.SortSpecification;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

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
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new SortLop(transformer.apply(input), columns);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, columns);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final SortLop other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(columns, other.columns);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("specification", columns)
                .append("input", input)
                .toString();
    }
}
