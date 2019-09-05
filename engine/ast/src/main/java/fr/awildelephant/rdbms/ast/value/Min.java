package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Objects;

public final class Min implements AST {

    private final AST input;

    private Min(AST input) {
        this.input = input;
    }

    public static AST min(AST input) {
        return new Min(input);
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
        if (!(obj instanceof Min)) {
            return false;
        }

        final Min other = (Min) obj;

        return Objects.equals(input, other.input);
    }
}
