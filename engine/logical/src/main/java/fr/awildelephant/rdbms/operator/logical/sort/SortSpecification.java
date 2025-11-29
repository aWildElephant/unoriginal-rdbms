package fr.awildelephant.rdbms.operator.logical.sort;

import fr.awildelephant.rdbms.schema.ColumnReference;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record SortSpecification(ColumnReference column, boolean ascending, boolean nullsLast) {

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("column", column)
                .append("ascending", ascending)
                .append("nullsLast", nullsLast)
                .toString();
    }
}
