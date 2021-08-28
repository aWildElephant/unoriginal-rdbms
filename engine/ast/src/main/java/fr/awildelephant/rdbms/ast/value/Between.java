package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class Between implements AST {

    private final AST value;
    private final AST lowerBound;
    private final AST upperBound;

    private Between(AST value, AST lowerBound, AST upperBound) {
        this.value = value;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static Between between(AST value, AST lowerBound, AST upperBound) {
        return new Between(value, lowerBound, upperBound);
    }

    public AST value() {
        return value;
    }

    public AST lowerBound() {
        return lowerBound;
    }

    public AST upperBound() {
        return upperBound;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, lowerBound, upperBound);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Between other)) {
            return false;
        }

        return Objects.equals(value, other.value)
                && Objects.equals(lowerBound, other.lowerBound)
                && Objects.equals(upperBound, other.upperBound);
    }
}
