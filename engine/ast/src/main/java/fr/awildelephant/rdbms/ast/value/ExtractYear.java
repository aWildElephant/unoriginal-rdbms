package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class ExtractYear extends UnaryNode<AST, AST> implements AST {

    private ExtractYear(AST child) {
        super(child);
    }

    public static ExtractYear extractYear(AST child) {
        return new ExtractYear(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ExtractYear other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
