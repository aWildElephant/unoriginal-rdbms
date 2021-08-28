package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class TextLiteral implements AST {

    private final String value;

    private TextLiteral(String value) {
        this.value = value;
    }

    public static TextLiteral textLiteral(String value) {
        return new TextLiteral(value);
    }

    public String value() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(value)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final TextLiteral other)) {
            return false;
        }

        return Objects.equals(value, other.value);
    }
}
