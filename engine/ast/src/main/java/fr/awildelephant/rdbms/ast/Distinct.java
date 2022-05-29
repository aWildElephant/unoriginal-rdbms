package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class Distinct extends UnaryNode<AST, AST> implements AST {

    public Distinct(AST child) {
        super(child);
    }

    public static Distinct distinct(AST child) {
        return new Distinct(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Distinct other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }

    @Override
    public String toString() {
        return "Distinct[child=" + child() + ']';
    }

}
