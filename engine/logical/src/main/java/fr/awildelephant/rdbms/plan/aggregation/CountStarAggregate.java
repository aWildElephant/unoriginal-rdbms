package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

public class CountStarAggregate implements Aggregate {

    @Override
    public boolean outputIsNullable() {
        return false;
    }

    @Override
    public ColumnReference outputName() {
        return new UnqualifiedColumnReference("count(*)");
    }

}
