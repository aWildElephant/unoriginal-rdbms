package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Multiply extends BinaryNode<AST, AST, AST> implements AST {

    private Multiply(AST leftChild, AST rightChild) {
        super(leftChild, rightChild);
    }

    public static Multiply multiply(final AST leftChild, final AST rightChild) {
        return new Multiply(leftChild, rightChild);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", leftChild())
                .append("right", rightChild())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Multiply other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
