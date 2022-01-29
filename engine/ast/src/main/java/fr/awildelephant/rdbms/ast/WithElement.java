package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record WithElement(String name, AST definition) implements AST {

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
                .append("definition", definition)
                .toString();
    }
}
