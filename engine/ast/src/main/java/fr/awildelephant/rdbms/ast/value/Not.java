package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class Not extends UnaryNode<AST, AST> implements AST {

    private Not(AST child) {
        super(child);
    }

    public static AST not(AST input) {
        return new Not(input);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Not other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
