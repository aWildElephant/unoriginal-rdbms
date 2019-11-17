package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

public final class CountAggregate implements Aggregate {

    private final ColumnReference input;
    private final boolean distinct;

    public CountAggregate(ColumnReference input, boolean distinct) {
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
    public ColumnReference outputName() {
        final String outputName;

        if (distinct) {
            outputName = "count(distinct " + input.fullName() + ")";
        } else {
            outputName = "count(" + input.fullName() + ")";
        }

        return new UnqualifiedColumnReference(outputName);
    }
}
