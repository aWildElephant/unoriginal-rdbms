package fr.awildelephant.rdbms.execution.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Objects;
import java.util.Optional;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class MinAggregate extends AbstractAggregate {

    private final ColumnReference input;

    public MinAggregate(ColumnReference input, ColumnReference output) {
        super(output);
        this.input = input;
    }

    public ColumnReference input() {
        return input;
    }

    @Override
    public boolean outputIsNullable() {
        return true;
    }

    @Override
    public Optional<ColumnReference> inputColumn() {
        return Optional.of(input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, outputColumn);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final MinAggregate other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(outputColumn, other.outputColumn);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("inputColumn", input)
                .append("outputColumn", outputColumn)
                .toString();
    }
}
