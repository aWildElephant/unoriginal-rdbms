package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Cast implements AST {

    private final AST input;
    private final int targetType;

    private Cast(final AST input, final int targetType) {
        this.input = input;
        this.targetType = targetType;
    }

    public static Cast cast(final AST input, final int targetType) {
        return new Cast(input, targetType);
    }

    public AST input() {
        return input;
    }

    public int targetType() {
        return targetType;
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, targetType);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof final Cast other)) {
            return false;
        }

        return targetType == other.targetType
                && Objects.equals(input, other.input);
    }
}
