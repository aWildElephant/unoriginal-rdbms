package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class Exists implements AST {

    private final AST input;

    private Exists(AST input) {
        this.input = input;
    }

    public static Exists exists(AST input) {
        return new Exists(input);
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
        if (!(obj instanceof Exists)) {
            return false;
        }

        final Exists other = (Exists) obj;

        return Objects.equals(input, other.input);
    }
}
