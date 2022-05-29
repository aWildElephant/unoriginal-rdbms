package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

public final class GreaterOrEqual extends BinaryNode<AST, AST, AST> implements AST {

    private GreaterOrEqual(AST leftChild, AST rightChild) {
        super(leftChild, rightChild);
    }

    public static GreaterOrEqual greaterOrEqual(AST leftChild, AST rightChild) {
        return new GreaterOrEqual(leftChild, rightChild);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final GreaterOrEqual other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
