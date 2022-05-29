package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class With extends BinaryNode<AST, WithList, AST> implements AST {

    public With(WithList withList, AST query) {
        super(withList, query);
    }

    public static With with(WithList withList, AST query) {
        return new With(withList, query);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("withList", firstChild())
                .append("query", secondChild())
                .toString();
    }

    public WithList withList() {
        return firstChild();
    }

    public AST query() {
        return secondChild();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof With other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
