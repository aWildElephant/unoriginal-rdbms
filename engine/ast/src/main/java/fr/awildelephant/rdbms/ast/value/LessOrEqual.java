package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

public final class LessOrEqual extends BinaryNode<AST, AST, AST> implements AST {

    private LessOrEqual(AST leftChild, AST rightChild) {
        super(leftChild, rightChild);
    }

    public static LessOrEqual lessOrEqual(AST left, AST right) {
        return new LessOrEqual(left, right);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LessOrEqual other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
