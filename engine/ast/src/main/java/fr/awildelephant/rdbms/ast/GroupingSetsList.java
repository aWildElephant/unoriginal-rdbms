package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class GroupingSetsList extends NAryNode<AST, ColumnName> implements AST {

    public GroupingSetsList(List<ColumnName> columns) {
        super(columns);
    }

    public static GroupingSetsList groupingSetsList(List<ColumnName> columns) {
        return new GroupingSetsList(columns);
    }

    public List<ColumnName> columns() {
        return children();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this).append(children()).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupingSetsList other)) {
            return false;
        }

        return equalsNAry(other);
    }
}
