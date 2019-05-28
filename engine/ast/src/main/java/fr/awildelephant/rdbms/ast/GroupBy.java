package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class GroupBy implements AST {

    private final AST input;
    private final GroupingSetsList groupingSpecification;

    private GroupBy(AST input, GroupingSetsList groupingSpecification) {
        this.input = input;
        this.groupingSpecification = groupingSpecification;
    }

    public static GroupBy groupBy(AST input, GroupingSetsList groupingSpecification) {
        return new GroupBy(input, groupingSpecification);
    }

    public AST input() {
        return input;
    }

    public GroupingSetsList groupingSpecification() {
        return groupingSpecification;
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
