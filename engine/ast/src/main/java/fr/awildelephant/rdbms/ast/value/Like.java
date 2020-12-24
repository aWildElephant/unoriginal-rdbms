package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Like implements AST {

    private final AST input;
    private final AST pattern;

    private Like(AST input, AST pattern) {
        this.input = input;
        this.pattern = pattern;
    }

    public static Like like(AST input, AST pattern) {
        return new Like(input, pattern);
    }

    public AST input() {
        return input;
    }

    public AST pattern() {
        return pattern;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("pattern", pattern)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, pattern);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Like)) {
            return false;
        }

        final Like other = (Like) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(pattern, other.pattern);
    }
}
