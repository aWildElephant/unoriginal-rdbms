package fr.awildelephant.rdbms.execution.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Objects;
import java.util.Optional;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class CountAggregate extends AbstractAggregate {

    private final ColumnReference input;
    private final boolean distinct;

    public CountAggregate(ColumnReference input, ColumnReference output, boolean distinct) {
        super(output);
        this.input = input;
        this.distinct = distinct;
    }

    public ColumnReference input() {
        return input;
    }

    public boolean distinct() {
        return distinct;
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
        return Objects.hash(distinct, input, outputColumn);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CountAggregate other)) {
            return false;
        }

        return distinct == other.distinct
                && Objects.equals(input, other.input)
                && Objects.equals(outputColumn, other.outputColumn);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("inputColumn", input)
                .append("outputColumn", outputColumn)
                .append("distinct", true)
                .toString();
    }
}
