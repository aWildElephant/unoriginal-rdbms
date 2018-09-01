package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Objects;

public final class Divide implements AST {

    private final AST left;
    private final AST right;

    private Divide(AST left, AST right) {
        this.left = left;
        this.right = right;
    }

    public static Divide divide(AST left, AST right) {
        return new Divide(left, right);
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
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Divide)) {
            return false;
        }

        final Divide other = (Divide) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}
