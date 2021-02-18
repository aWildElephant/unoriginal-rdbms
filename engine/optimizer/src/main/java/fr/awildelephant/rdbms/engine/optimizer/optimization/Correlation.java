package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(innerColumn, outerColumn);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Correlation)) {
            return false;
        }

        final Correlation other = (Correlation) o;

        return Objects.equals(innerColumn, other.innerColumn)
                && Objects.equals(outerColumn, other.outerColumn);
    }
}
