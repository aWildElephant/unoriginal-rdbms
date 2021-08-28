package fr.awildelephant.rdbms.plan;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ScalarSubqueryLop extends AbstractLop {

    private final LogicalOperator input;

    public ScalarSubqueryLop(LogicalOperator input) {
        super(input.schema());

        this.input = input;
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new ScalarSubqueryLop(transformer.apply(input));
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
        if (!(obj instanceof final ScalarSubqueryLop other)) {
            return false;
        }

        return Objects.equals(input, other.input);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .toString();
    }
}
