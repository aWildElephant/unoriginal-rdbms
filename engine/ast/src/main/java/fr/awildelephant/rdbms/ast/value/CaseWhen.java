package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class CaseWhen implements AST {

    private final AST condition;
    private final AST thenExpression;
    private final AST elseExpression;

    private CaseWhen(AST condition, AST thenExpression, AST elseExpression) {
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    public static CaseWhen caseWhen(AST condition, AST thenExpression, AST elseExpression) {
        return new CaseWhen(condition, thenExpression, elseExpression);
    }

    public AST condition() {
        return condition;
    }

    public AST thenExpression() {
        return thenExpression;
    }

    public AST elseExpression() {
        return elseExpression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, thenExpression, elseExpression);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CaseWhen other)) {
            return false;
        }

        return Objects.equals(condition, other.condition)
                && Objects.equals(thenExpression, other.thenExpression)
                && Objects.equals(elseExpression, other.elseExpression);
    }
}
