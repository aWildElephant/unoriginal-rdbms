package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class Avg extends UnaryNode<AST, AST> implements AST {

    private Avg(AST child) {
        super(child);
    }

    public static Avg avg(AST child) {
        return new Avg(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Avg other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
