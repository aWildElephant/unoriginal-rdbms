package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.annotation.Intermediate;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

// TODO: unused, remove ?
@Intermediate
public final class AntiJoin implements AST {

    private final AST left;
    private final AST right;

    private AntiJoin(AST left, AST right) {
        this.left = left;
        this.right = right;
    }

    public static AntiJoin antiJoin(AST left, AST right) {
        return new AntiJoin(left, right);
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
        if (!(obj instanceof AntiJoin)) {
            return false;
        }

        final AntiJoin other = (AntiJoin) obj;

        return Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }
}

