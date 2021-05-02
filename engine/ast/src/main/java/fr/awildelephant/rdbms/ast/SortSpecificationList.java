package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class SortSpecificationList implements AST {

    private final List<SortSpecification> sortSpecificationList;

    private SortSpecificationList(List<SortSpecification> sortSpecificationList) {
        this.sortSpecificationList = sortSpecificationList;
    }

    public static SortSpecificationList sortSpecificationList(List<SortSpecification> columns) {
        return new SortSpecificationList(columns);
    }

    public List<SortSpecification> columns() {
        return sortSpecificationList;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(sortSpecificationList)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sortSpecificationList);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortSpecificationList)) {
            return false;
        }

        final SortSpecificationList other = (SortSpecificationList) obj;

        return Objects.equals(sortSpecificationList, other.sortSpecificationList);
    }
}
