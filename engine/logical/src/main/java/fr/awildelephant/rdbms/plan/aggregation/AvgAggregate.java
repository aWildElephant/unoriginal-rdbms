package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.Objects;

public final class AvgAggregate implements Aggregate {

    private final ColumnReference input;

    public AvgAggregate(ColumnReference input) {
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
    public ColumnReference outputName() {
        return new UnqualifiedColumnReference("avg(" + input.fullName() + ")");
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AvgAggregate)) {
            return false;
        }

        final AvgAggregate other = (AvgAggregate) obj;

        return Objects.equals(input, other.input);
    }
}
