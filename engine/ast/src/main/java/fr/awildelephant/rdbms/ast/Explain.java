package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Explain implements AST {

    private final AST input;

    public Explain(AST input) {
        this.input = input;
    }

    public static Explain explain(AST input) {
        return new Explain(input);
    }

    public AST input() {
        return input;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(input)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Explain other)) {
            return false;
        }

        return Objects.equals(input, other.input);
    }
}
