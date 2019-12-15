package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class LeftJoin implements AST {

    private final AST left;
    private final AST right;
    private final AST joinSpecification;

    private LeftJoin(AST left, AST right, AST joinSpecification) {
        this.left = left;
        this.right = right;
        this.joinSpecification = joinSpecification;
    }

    public static LeftJoin leftJoin(AST left, AST right, AST joinSpecification) {
        return new LeftJoin(left, right, joinSpecification);
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
        if (!(obj instanceof LeftJoin)) {
            return false;
        }

        final LeftJoin other = (LeftJoin) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right)
                && Objects.equals(joinSpecification, other.joinSpecification);
    }
}
