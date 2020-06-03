package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.annotation.Intermediate;

import java.util.Objects;

@Intermediate
public final class SemiJoin implements AST {

    private final AST left;
    private final AST right;
    private final AST predicate;

    private SemiJoin(AST left, AST right, AST predicate) {
        this.left = left;
        this.right = right;
        this.predicate = predicate;
    }

    public static SemiJoin semiJoin(AST left, AST right, AST predicate) {
        return new SemiJoin(left, right, predicate);
    }

    public AST left() {
        return left;
    }

    public AST right() {
        return right;
    }

    public AST predicate() {
        return predicate;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, predicate);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SemiJoin)) {
            return false;
        }

        final SemiJoin other = (SemiJoin) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right)
                && Objects.equals(predicate, other.predicate);
    }
}
