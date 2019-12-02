package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.Objects;

public final class MaxAggregate implements Aggregate {

    private final ColumnReference input;

    public MaxAggregate(ColumnReference input) {
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
        return new UnqualifiedColumnReference("max(" + input.fullName() + ")");
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MaxAggregate)) {
            return false;
        }

        final MaxAggregate other = (MaxAggregate) obj;

        return Objects.equals(input, other.input);
    }
}
