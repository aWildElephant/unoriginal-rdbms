package fr.awildelephant.rdbms.execution.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;

public abstract class AbstractAggregate implements Aggregate {

    protected final ColumnReference outputColumn;

    protected AbstractAggregate(ColumnReference outputColumn) {
        this.outputColumn = outputColumn;
    }

    @Override
    public ColumnReference outputColumn() {
        return outputColumn;
    }
}
