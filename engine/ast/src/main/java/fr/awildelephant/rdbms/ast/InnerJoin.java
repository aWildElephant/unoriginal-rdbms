package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.TernaryNode;

public final class InnerJoin extends TernaryNode<AST, AST, AST, AST> implements AST {

    public InnerJoin(AST left, AST right, AST joinSpecification) {
        super(left, right, joinSpecification);
    }

    public static InnerJoin innerJoin(AST left, AST right, AST joinSpecification) {
        return new InnerJoin(left, right, joinSpecification);
    }

    public AST left() {
        return firstChild();
    }

    public AST right() {
        return secondChild();
    }

    public AST specification() {
        return thirdChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "InnerJoin[left=" + firstChild() + ", right=" + secondChild() + ", specification=" + thirdChild() + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InnerJoin other)) {
            return false;
        }

        return equalsTernary(other);
    }
}
