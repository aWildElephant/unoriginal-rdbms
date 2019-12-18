package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.schema.ColumnReference;

public final class Correlation {

    private final ColumnReference innerColumn;
    private final ColumnReference outerColumn;

    public Correlation(ColumnReference innerColumn, ColumnReference outerColumn) {
        this.innerColumn = innerColumn;
        this.outerColumn = outerColumn;
    }

    public ColumnReference getInnerColumn() {
        return innerColumn;
    }

    public ColumnReference getOuterColumn() {
        return outerColumn;
    }
}
