package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class And extends BinaryNode<AST, AST, AST> implements AST {

    private And(AST leftChild, AST rightChild) {
        super(leftChild, rightChild);
    }

    public static And and(AST leftChild, AST rightChild) {
        return new And(leftChild, rightChild);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("leftChild", leftChild())
                .append("rightChild", rightChild())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof And other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
