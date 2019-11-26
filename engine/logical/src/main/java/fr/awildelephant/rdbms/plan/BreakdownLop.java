package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class BreakdownLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ColumnReference> breakdowns;

    public BreakdownLop(LogicalOperator input, List<ColumnReference> breakdowns) {
        super(input.schema());

        this.input = input;
        this.breakdowns = breakdowns;
    }

    public LogicalOperator input() {
        return input;
    }

    public List<ColumnReference> breakdowns() {
        return breakdowns;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new BreakdownLop(transformer.apply(input), breakdowns);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, breakdowns);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BreakdownLop)) {
            return false;
        }

        final BreakdownLop other = (BreakdownLop) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(breakdowns, other.breakdowns);
    }
}
