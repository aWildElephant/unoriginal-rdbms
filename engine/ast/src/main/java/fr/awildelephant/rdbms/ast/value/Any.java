package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Any implements AST {

    private final AST input;

    private Any(AST input) {
        this.input = input;
    }

    public static Any any(AST input) {
        return new Any(input);
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
        if (!(obj instanceof Any)) {
            return false;
        }

        final Any other = (Any) obj;

        return Objects.equals(input, other.input);
    }
}
