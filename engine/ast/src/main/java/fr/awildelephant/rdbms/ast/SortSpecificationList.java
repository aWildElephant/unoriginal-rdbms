package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class SortSpecificationList extends NAryNode<AST, SortSpecification> implements AST {

    public SortSpecificationList(List<SortSpecification> sortSpecificationList) {
        super(sortSpecificationList);
    }

    public static SortSpecificationList sortSpecificationList(List<SortSpecification> columns) {
        return new SortSpecificationList(columns);
    }

    public List<SortSpecification> sortSpecificationList() {
        return children();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(children())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortSpecificationList other)) {
            return false;
        }

        return equalsNAry(other);
    }
}
