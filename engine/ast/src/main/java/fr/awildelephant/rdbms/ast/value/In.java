package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class In implements AST {

    private final AST input;
    private final AST value;

    private In(AST input, AST value) {
        this.input = input;
        this.value = value;
    }

    public static In in(AST input, AST value) {
        return new In(input, value);
    }

    public AST input() {
        return input;
    }

    public AST value() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final In other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(value, other.value);
    }
}
