package fr.awildelephant.rdbms.plan;

import java.util.Objects;

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
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, limit);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LimitLop)) {
            return false;
        }

        final LimitLop other = (LimitLop) obj;

        return limit == other.limit
                && Objects.equals(input, other.input);
    }
}
