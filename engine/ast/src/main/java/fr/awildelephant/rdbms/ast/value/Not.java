package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Not implements AST {

    private final AST input;

    private Not(AST input) {
        this.input = input;
    }

    public static AST not(AST input) {
        return new Not(input);
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
        if (!(obj instanceof final Not other)) {
            return false;
        }

        return Objects.equals(input, other.input);
    }
}
