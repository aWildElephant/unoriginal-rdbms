package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public final class Like implements AST {

    private final AST value;
    private final AST pattern;

    private Like(AST value, AST pattern) {
        this.value = value;
        this.pattern = pattern;
    }

    public static Like like(AST value, AST pattern) {
        return new Like(value, pattern);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("pattern", pattern)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, pattern);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Like)) {
            return false;
        }

        final Like other = (Like) obj;

        return Objects.equals(value, other.value)
                && Objects.equals(pattern, other.pattern);
    }
}
