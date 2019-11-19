package fr.awildelephant.rdbms.plan;

import java.util.Objects;

public final class DistinctLop extends AbstractLop {

    private final LogicalOperator input;

    public DistinctLop(LogicalOperator input) {
        super(input.schema());
        this.input = input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DistinctLop)) {
            return false;
        }

        final DistinctLop other = (DistinctLop) obj;

        return Objects.equals(input, other.input);
    }
}
