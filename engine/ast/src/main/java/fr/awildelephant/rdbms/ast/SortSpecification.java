package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class SortSpecification extends UnaryNode<AST, ColumnName> implements AST {

    private final OrderingSpecification ordering;

    public SortSpecification(ColumnName sortKey, OrderingSpecification ordering) {
        super(sortKey);
        this.ordering = ordering;
    }

    public static SortSpecification sortSpecification(ColumnName sortKey, OrderingSpecification ordering) {
        return new SortSpecification(sortKey, ordering);
    }

    public ColumnName sortKey() {
        return child();
    }

    public OrderingSpecification ordering() {
        return ordering;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("sortKey", child())
                .append("ordering", ordering)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortSpecification other)) {
            return false;
        }

        return Objects.equals(ordering, other.ordering)
                && equalsUnaryNode(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child(), ordering);
    }

}
