package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record GroupingSetsList(List<ColumnName> columns) implements AST {

    public static GroupingSetsList groupingSetsList(List<ColumnName> columns) {
        return new GroupingSetsList(columns);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(columns)
                .toString();
    }
}
