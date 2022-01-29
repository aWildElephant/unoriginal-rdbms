package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record SortSpecificationList(List<SortSpecification> sortSpecificationList) implements AST {

    public static SortSpecificationList sortSpecificationList(List<SortSpecification> columns) {
        return new SortSpecificationList(columns);
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
}
