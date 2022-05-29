package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class Exists extends UnaryNode<AST, AST> implements AST {

    public Exists(AST child) {
        super(child);
    }

    public static Exists exists(AST child) {
        return new Exists(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Exists[child=" + child() + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Exists other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }

}
