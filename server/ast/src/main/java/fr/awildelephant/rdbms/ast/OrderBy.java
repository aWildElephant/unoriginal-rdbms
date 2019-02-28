package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class OrderBy implements AST {

    private final AST input;
    private final SortSpecificationList sortSpecification;

    private OrderBy(AST input, SortSpecificationList sortSpecification) {
        this.input = input;
        this.sortSpecification = sortSpecification;
    }

    public static OrderBy orderBy(AST input, SortSpecificationList sortSpecification) {
        return new OrderBy(input, sortSpecification);
    }

    public AST input() {
        return input;
    }

    public SortSpecificationList sortSpecification() {
        return sortSpecification;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, sortSpecification);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderBy)) {
            return false;
        }

        final OrderBy other = (OrderBy) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(sortSpecification, other.sortSpecification);
    }
}
