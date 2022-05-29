package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

public final class Less extends BinaryNode<AST, AST, AST> implements AST {

    private Less(AST leftChild, AST rightChild) {
        super(leftChild, rightChild);
    }

    public static Less less(AST leftChild, AST rightChild) {
        return new Less(leftChild, rightChild);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return leftChild() + " < " + rightChild();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Less other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
