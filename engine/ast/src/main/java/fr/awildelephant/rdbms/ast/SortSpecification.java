package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record SortSpecification(ColumnName sortKey, OrderingSpecification ordering) implements AST {

    public static SortSpecification sortSpecification(ColumnName sortKey, OrderingSpecification ordering) {
        return new SortSpecification(sortKey, ordering);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("sortKey", sortKey)
                .append("ordering", ordering)
                .toString();
    }
}
