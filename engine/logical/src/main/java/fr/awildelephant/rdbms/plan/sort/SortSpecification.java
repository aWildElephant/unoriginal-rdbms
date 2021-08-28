package fr.awildelephant.rdbms.plan.sort;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class SortSpecification {

    private final ColumnReference column;
    private final boolean ascending;

    private SortSpecification(ColumnReference column, boolean ascending) {
        this.column = column;
        this.ascending = ascending;
    }

    public static SortSpecification ascending(ColumnReference column) {
        return new SortSpecification(column, true);
    }

    public static SortSpecification descending(ColumnReference column) {
        return new SortSpecification(column, false);
    }

    public ColumnReference column() {
        return column;
    }

    public boolean ascending() {
        return ascending;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, ascending);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final SortSpecification other)) {
            return false;
        }

        return ascending == other.ascending
                && Objects.equals(column, other.column);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("column", column)
                .append("ascending", ascending)
                .toString();
    }
}
