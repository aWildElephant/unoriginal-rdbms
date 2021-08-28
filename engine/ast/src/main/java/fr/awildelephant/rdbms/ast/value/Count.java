package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Count implements AST {

    private final boolean distinct;
    private final AST input;

    private Count(boolean distinct, AST input) {
        this.distinct = distinct;
        this.input = input;
    }

    public static Count count(boolean distinct, AST input) {
        return new Count(distinct, input);
    }

    public boolean distinct() {
        return distinct;
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
        return Objects.hash(distinct, input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Count other)) {
            return false;
        }

        return distinct == other.distinct
                && Objects.equals(input, other.input);
    }
}
