package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.Objects;
import java.util.function.Function;

public final class FilterLop extends AbstractLop {

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
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new FilterLop(transformer.apply(input), filter);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, filter);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FilterLop)) {
            return false;
        }

        final FilterLop other = (FilterLop) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(filter, other.filter);
    }
}
