package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.ordering.NullsHandling;
import fr.awildelephant.rdbms.ast.ordering.OrderingSpecification;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.ordering.NullsHandling.UNSPECIFIED;
import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class SortSpecification extends UnaryNode<AST, ColumnName> implements AST {

    private final OrderingSpecification ordering;
    private final NullsHandling nullsHandling;

    public SortSpecification(ColumnName sortKey, OrderingSpecification ordering, NullsHandling nullsHandling) {
        super(sortKey);
        this.ordering = ordering;
        this.nullsHandling = nullsHandling;
    }

    public static SortSpecification sortSpecification(ColumnName sortKey, OrderingSpecification ordering) {
        return new SortSpecification(sortKey, ordering, UNSPECIFIED);
    }

    public static SortSpecification sortSpecification(ColumnName sortKey, OrderingSpecification ordering, NullsHandling nullsHandling) {
        return new SortSpecification(sortKey, ordering, nullsHandling);
    }

    public ColumnName sortKey() {
        return child();
    }

    public OrderingSpecification ordering() {
        return ordering;
    }

    public NullsHandling nullsHandling() {
        return nullsHandling;
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
                .append("nullsHandling", nullsHandling)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortSpecification other)) {
            return false;
        }

        return Objects.equals(ordering, other.ordering)
                && Objects.equals(nullsHandling, other.nullsHandling)
                && equalsUnaryNode(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child(), ordering, nullsHandling);
    }

}
