package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Sum extends UnaryNode<AST, AST> implements AST {

    private Sum(AST child) {
        super(child);
    }

    public static Sum sum(AST child) {
        return new Sum(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Sum other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(child())
                .toString();
    }
}
