package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class WithElement extends UnaryNode<AST, AST> implements AST {

    private final String name;

    public WithElement(String name, AST definition) {
        super(definition);
        this.name = name;
    }

    public static WithElement withElement(String name, AST definition) {
        return new WithElement(name, definition);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("name", name)
                .append("definition", child())
                .toString();
    }

    public String name() {
        return name;
    }

    public AST definition() {
        return child();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WithElement other)) {
            return false;
        }

        return Objects.equals(name, other.name)
                && equalsUnaryNode(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, child());
    }
}
