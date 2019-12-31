package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.Objects;
import java.util.Optional;

public final class AnyAggregate implements Aggregate {

    private final ColumnReference input;

    public AnyAggregate(ColumnReference input) {
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
        return new UnqualifiedColumnReference("any(" + input.fullName() + ')');
    }

    @Override
    public Optional<ColumnReference> inputColumn() {
        return Optional.of(input);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnyAggregate)) {
            return false;
        }

        final AnyAggregate other = (AnyAggregate) obj;

        return Objects.equals(input, other.input);
    }
}
