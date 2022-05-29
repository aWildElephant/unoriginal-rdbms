package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.TernaryNode;

public final class CaseWhen extends TernaryNode<AST, AST, AST, AST> implements AST {

    private CaseWhen(AST condition, AST thenExpression, AST elseExpression) {
        super(condition, thenExpression, elseExpression);
    }

    public static CaseWhen caseWhen(AST condition, AST thenExpression, AST elseExpression) {
        return new CaseWhen(condition, thenExpression, elseExpression);
    }

    public AST condition() {
        return firstChild();
    }

    public AST thenExpression() {
        return secondChild();
    }

    public AST elseExpression() {
        return thirdChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CaseWhen other)) {
            return false;
        }

        return equalsTernary(other);
    }
}
