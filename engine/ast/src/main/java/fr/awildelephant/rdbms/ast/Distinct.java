package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Distinct implements AST {

    private final AST input;

    private Distinct(AST input) {
        this.input = input;
    }

    public static Distinct distinct(AST input) {
        return new Distinct(input);
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
        if (!(obj instanceof final Distinct other)) {
            return false;
        }

        return Objects.equals(input, other.input);
    }
}
