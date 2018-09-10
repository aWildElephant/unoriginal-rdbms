package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class GroupBy implements AST {

    private final AST input;
    private final AST groupingSpecification;

    private GroupBy(AST input, AST groupingSpecification) {
        this.input = input;
        this.groupingSpecification = groupingSpecification;
    }

    public static GroupBy groupBy(AST input, AST groupingSpecification) {
        return new GroupBy(input, groupingSpecification);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, groupingSpecification);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupBy)) {
            return false;
        }

        final GroupBy other = (GroupBy) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(groupingSpecification, other.groupingSpecification);
    }
}
