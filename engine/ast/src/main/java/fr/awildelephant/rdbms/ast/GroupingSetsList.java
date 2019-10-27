package fr.awildelephant.rdbms.ast;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class GroupingSetsList implements AST {

    private final List<IdentifierChain> columns;

    private GroupingSetsList(List<IdentifierChain> columns) {
        this.columns = columns;
    }

    public static GroupingSetsList groupingSetsList(List<IdentifierChain> columns) {
        return new GroupingSetsList(columns);
    }

    public List<String> breakdowns() {
        return columns.stream().map(IdentifierChain::last).collect(Collectors.toList());
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
