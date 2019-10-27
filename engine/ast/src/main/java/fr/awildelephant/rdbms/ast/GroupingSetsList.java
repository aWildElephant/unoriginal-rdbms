package fr.awildelephant.rdbms.ast;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class GroupingSetsList implements AST {

    private final List<ColumnReference> columns;

    private GroupingSetsList(List<ColumnReference> columns) {
        this.columns = columns;
    }

    public static GroupingSetsList groupingSetsList(List<ColumnReference> columns) {
        return new GroupingSetsList(columns);
    }

    public List<String> breakdowns() {
        return columns.stream().map(ColumnReference::name).collect(Collectors.toList());
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
