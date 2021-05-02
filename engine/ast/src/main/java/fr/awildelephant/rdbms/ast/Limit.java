package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Limit implements AST {

    private final AST input;
    private final int limit;

    private Limit(AST input, int limit) {
        this.input = input;
        this.limit = limit;
    }

    public static Limit limit(AST input, int limit) {
        return new Limit(input, limit);
    }

    public AST input() {
        return input;
    }

    public int limit() {
        return limit;
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("limit", limit)
                .toString();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, limit);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Limit)) {
            return false;
        }

        final Limit other = (Limit) obj;

        return limit == other.limit
                && Objects.equals(input, other.input);
    }
}
