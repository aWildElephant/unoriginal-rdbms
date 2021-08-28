package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Plus implements AST {

    private final AST left;
    private final AST right;

    private Plus(AST left, AST right) {
        this.left = left;
        this.right = right;
    }

    public static Plus plus(AST left, AST right) {
        return new Plus(left, right);
    }

    public AST left() {
        return left;
    }

    public AST right() {
        return right;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("left", left)
                .append("right", right)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Plus other)) {
            return false;
        }

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
