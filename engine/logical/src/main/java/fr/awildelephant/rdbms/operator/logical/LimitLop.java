package fr.awildelephant.rdbms.operator.logical;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

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
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new LimitLop(transformer.apply(input), limit);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, limit);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LimitLop other)) {
            return false;
        }

        return limit == other.limit
                && Objects.equals(input, other.input);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("limit", limit)
                .append("input", input)
                .toString();
    }
}
