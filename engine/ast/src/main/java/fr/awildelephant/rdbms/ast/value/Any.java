package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class Any extends UnaryNode<AST, AST> implements AST {

    private Any(AST child) {
        super(child);
    }

    public static Any any(AST child) {
        return new Any(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Any other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
