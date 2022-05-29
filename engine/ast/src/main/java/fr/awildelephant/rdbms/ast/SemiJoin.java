package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.annotation.Intermediate;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.TernaryNode;

@Intermediate
public final class SemiJoin extends TernaryNode<AST, AST, AST, AST> implements AST {

    public SemiJoin(AST left, AST right, AST predicate) {
        super(left, right, predicate);
    }

    public static SemiJoin semiJoin(AST left, AST right, AST predicate) {
        return new SemiJoin(left, right, predicate);
    }

    public AST left() {
        return firstChild();
    }

    public AST right() {
        return secondChild();
    }

    public AST predicate() {
        return thirdChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "SemiJoin[left=" + firstChild() + ", right=" + secondChild() + ", predicate=" + thirdChild() + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SemiJoin other)) {
            return false;
        }

        return equalsTernary(other);
    }
}
