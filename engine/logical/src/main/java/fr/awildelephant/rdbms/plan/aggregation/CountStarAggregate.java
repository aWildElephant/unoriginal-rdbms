package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.Optional;

public final class CountStarAggregate implements Aggregate {

    private static final CountStarAggregate INSTANCE = new CountStarAggregate();

    private CountStarAggregate() {

    }

    public static CountStarAggregate countStarAggregate() {
        return INSTANCE;
    }

    @Override
    public boolean outputIsNullable() {
        return false;
    }

    @Override
    public ColumnReference outputName() {
        return new UnqualifiedColumnReference("count(*)");
    }

    @Override
    public Optional<ColumnReference> inputColumn() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
