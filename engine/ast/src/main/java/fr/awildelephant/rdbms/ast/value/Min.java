package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Min extends UnaryNode<AST, AST> implements AST {

    private Min(AST child) {
        super(child);
    }

    public static AST min(AST child) {
        return new Min(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(child())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Min other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
