package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

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
        if (!(obj instanceof final GroupingSetsList other)) {
            return false;
        }

        return Objects.equals(columns, other.columns);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(columns)
                .toString();
    }
}
