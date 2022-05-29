package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

public final class In extends BinaryNode<AST, AST, AST> implements AST {

    private In(AST input, AST value) {
        super(input, value);
    }

    public static In in(AST input, AST value) {
        return new In(input, value);
    }

    public AST input() {
        return firstChild();
    }

    public AST value() {
        return secondChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final In other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}
