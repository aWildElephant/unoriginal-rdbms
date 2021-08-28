package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Avg implements AST {

    private final AST input;

    private Avg(AST input) {
        this.input = input;
    }

    public static Avg avg(AST input) {
        return new Avg(input);
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
        if (!(obj instanceof final Avg other)) {
            return false;
        }

        return Objects.equals(input, other.input);
    }
}
