package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class WithElement implements AST {

    private final String name;
    private final AST definition;

    private WithElement(String name, AST definition) {
        this.name = name;
        this.definition = definition;
    }

    public static WithElement withElement(String name, AST definition) {
        return new WithElement(name, definition);
    }

    public String name() {
        return name;
    }

    public AST definition() {
        return definition;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, definition);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WithElement)) {
            return false;
        }

        final WithElement other = (WithElement) obj;

        return Objects.equals(name, other.name)
                && Objects.equals(definition, other.definition);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("name", name)
                .append("definition", definition)
                .toString();
    }
}
