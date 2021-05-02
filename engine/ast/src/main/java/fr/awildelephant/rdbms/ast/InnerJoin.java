package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class InnerJoin implements AST {

    private final AST left;
    private final AST right;
    private final AST joinSpecification;

    private InnerJoin(AST left, AST right, AST joinSpecification) {
        this.left = left;
        this.right = right;
        this.joinSpecification = joinSpecification;
    }

    public static InnerJoin innerJoin(AST left, AST right, AST joinSpecification) {
        return new InnerJoin(left, right, joinSpecification);
    }

    public AST left() {
        return left;
    }

    public AST right() {
        return right;
    }

    public AST joinSpecification() {
        return joinSpecification;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, joinSpecification);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InnerJoin)) {
            return false;
        }

        final InnerJoin other = (InnerJoin) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right)
                && Objects.equals(joinSpecification, other.joinSpecification);
    }
}
