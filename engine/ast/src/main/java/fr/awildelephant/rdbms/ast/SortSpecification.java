package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class SortSpecification implements AST {

    private final ColumnName sortKey;
    private final OrderingSpecification ordering;

    private SortSpecification(ColumnName sortKey, OrderingSpecification ordering) {
        this.sortKey = sortKey;
        this.ordering = ordering;
    }

    public static SortSpecification sortSpecification(ColumnName sortKey, OrderingSpecification ordering) {
        return new SortSpecification(sortKey, ordering);
    }

    public ColumnName sortKey() {
        return sortKey;
    }

    public OrderingSpecification orderingSpecification() {
        return ordering;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return sortKey + " " + ordering.name();
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortKey, ordering);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortSpecification)) {
            return false;
        }

        final SortSpecification other = (SortSpecification) obj;
        return Objects.equals(sortKey, other.sortKey)
                && ordering == other.ordering;
    }
}
