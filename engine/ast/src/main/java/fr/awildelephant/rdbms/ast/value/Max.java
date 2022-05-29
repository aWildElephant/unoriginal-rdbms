package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class Max extends UnaryNode<AST, AST> implements AST {

    private Max(AST child) {
        super(child);
    }

    public static Max max(AST child) {
        return new Max(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Max other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
