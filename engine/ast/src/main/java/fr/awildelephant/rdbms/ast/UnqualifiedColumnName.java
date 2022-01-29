package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record UnqualifiedColumnName(String name) implements ColumnName {

    public static UnqualifiedColumnName unqualifiedColumnName(String name) {
        return new UnqualifiedColumnName(name);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
