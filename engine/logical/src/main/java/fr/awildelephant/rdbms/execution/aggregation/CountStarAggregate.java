package fr.awildelephant.rdbms.execution.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Objects;
import java.util.Optional;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class CountStarAggregate extends AbstractAggregate {

    public CountStarAggregate(ColumnReference output) {
        super(output);
    }

    @Override
    public boolean outputIsNullable() {
        return false;
    }

    @Override
    public Optional<ColumnReference> inputColumn() {
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(outputColumn);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CountStarAggregate other)) {
            return false;
        }

        return Objects.equals(outputColumn, other.outputColumn);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("outputColumn", outputColumn())
                .toString();
    }
}
