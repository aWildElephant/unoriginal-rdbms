package fr.awildelephant.rdbms.ast;

import java.util.List;
import java.util.Objects;

public final class SortSpecificationList implements AST {

    private final List<ColumnName> columns;

    private SortSpecificationList(List<ColumnName> columns) {
        this.columns = columns;
    }

    public static SortSpecificationList sortSpecificationList(List<ColumnName> columns) {
        return new SortSpecificationList(columns);
    }

    public List<ColumnName> columns() {
        return columns;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(columns);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortSpecificationList)) {
            return false;
        }

        final SortSpecificationList other = (SortSpecificationList) obj;

        return Objects.equals(columns, other.columns);
    }
}
