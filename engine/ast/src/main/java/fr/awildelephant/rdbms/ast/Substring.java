package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Substring implements AST {

    private final AST input;
    private final AST start;
    private final AST length;

    private Substring(AST input, AST start, AST length) {
        this.input = input;
        this.start = start;
        this.length = length;
    }

    public static Substring substring(AST input, AST start, AST length) {
        return new Substring(input, start, length);
    }

    public AST input() {
        return input;
    }

    public AST start() {
        return start;
    }

    public AST length() {
        return length;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, start, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Substring)) {
            return false;
        }

        final Substring other = (Substring) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(start, other.start)
                && Objects.equals(length, other.length);
    }
}
