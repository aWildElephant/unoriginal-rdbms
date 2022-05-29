package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class WithList extends NAryNode<AST, WithElement> implements AST {

    public WithList(List<WithElement> children) {
        super(children);
    }

    public static WithList withList(List<WithElement> children) {
        return new WithList(children);
    }

    public List<WithElement> elements() {
        return children();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("elements", children())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WithList other)) {
            return false;
        }

        return equalsNAry(other);
    }
}
