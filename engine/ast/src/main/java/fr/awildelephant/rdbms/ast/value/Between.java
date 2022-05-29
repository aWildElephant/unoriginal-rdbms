package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.TernaryNode;

public final class Between extends TernaryNode<AST, AST, AST, AST> implements AST {

    private Between(AST value, AST lowerBound, AST upperBound) {
        super(value, lowerBound, upperBound);
    }

    public static Between between(AST value, AST lowerBound, AST upperBound) {
        return new Between(value, lowerBound, upperBound);
    }

    public AST value() {
        return firstChild();
    }

    public AST lowerBound() {
        return secondChild();
    }

    public AST upperBound() {
        return thirdChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Between other)) {
            return false;
        }

        return equalsTernary(other);
    }
}
