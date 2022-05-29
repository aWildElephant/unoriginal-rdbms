package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Plus extends BinaryNode<AST, AST, AST> implements AST {

    private Plus(AST leftChild, AST rightChild) {
        super(leftChild, rightChild);
    }

    public static Plus plus(AST leftChild, AST rightChild) {
        return new Plus(leftChild, rightChild);
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
        if (!(obj instanceof final Plus other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
