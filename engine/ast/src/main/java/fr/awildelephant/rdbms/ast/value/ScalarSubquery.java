package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class ScalarSubquery extends UnaryNode<AST, AST> implements AST {

    private ScalarSubquery(AST child) {
        super(child);
    }

    public static ScalarSubquery scalarSubquery(AST child) {
        return new ScalarSubquery(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ScalarSubquery other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
