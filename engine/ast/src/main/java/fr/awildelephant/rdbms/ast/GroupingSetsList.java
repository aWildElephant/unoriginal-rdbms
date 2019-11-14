package fr.awildelephant.rdbms.ast;

import java.util.List;
import java.util.Objects;

public final class GroupingSetsList implements AST {

    private final List<ColumnName> columns;

    private GroupingSetsList(List<ColumnName> columns) {
        this.columns = columns;
    }

    public static GroupingSetsList groupingSetsList(List<ColumnName> columns) {
        return new GroupingSetsList(columns);
    }

    public List<ColumnName> breakdowns() {
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
        if (!(obj instanceof GroupingSetsList)) {
            return false;
        }

        final GroupingSetsList other = (GroupingSetsList) obj;

        return Objects.equals(columns, other.columns);
    }
}
