package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Greater implements AST {

    private final AST left;
    private final AST right;

    private Greater(AST left, AST right) {
        this.left = left;
        this.right = right;
    }

    public static Greater greater(AST left, AST right) {
        return new Greater(left, right);
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
        return left + " > " + right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Greater other)) {
            return false;
        }

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
