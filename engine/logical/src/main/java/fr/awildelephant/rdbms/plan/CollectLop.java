package fr.awildelephant.rdbms.plan;

import java.util.Objects;
import java.util.function.Function;

public final class CollectLop extends AbstractLop {

    private final LogicalOperator input;

    public CollectLop(LogicalOperator input) {
        super(input.schema());

        this.input = input;
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new CollectLop(transformer.apply(input));
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CollectLop)) {
            return false;
        }

        final CollectLop other = (CollectLop) obj;

        return Objects.equals(input, other.input);
    }
}
