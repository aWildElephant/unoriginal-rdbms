package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ProjectionLop extends AbstractLop {

    private final LogicalOperator input;
    private final List<ColumnReference> outputColumns;

    public ProjectionLop(LogicalOperator input, List<ColumnReference> outputColumns) {
        super(input.schema().project(outputColumns));

        this.input = input;
        this.outputColumns = outputColumns;
    }

    public LogicalOperator input() {
        return input;
    }

    public List<ColumnReference> outputColumns() {
        return outputColumns;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new ProjectionLop(transformer.apply(input), outputColumns);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, outputColumns);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ProjectionLop other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(outputColumns, other.outputColumns);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("projection", outputColumns)
                .append("input", input)
                .toString();
    }
}
