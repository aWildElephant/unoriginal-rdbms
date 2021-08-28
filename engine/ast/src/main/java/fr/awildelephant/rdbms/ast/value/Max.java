package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Max implements AST {

    private final AST input;

    private Max(AST input) {
        this.input = input;
    }

    public static Max max(AST input) {
        return new Max(input);
    }

    public AST input() {
        return input;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Max other)) {
            return false;
        }

        return Objects.equals(input, other.input);
    }
}
