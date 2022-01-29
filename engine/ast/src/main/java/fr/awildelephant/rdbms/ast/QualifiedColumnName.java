package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record QualifiedColumnName(String qualifier, String name) implements ColumnName {

    public static QualifiedColumnName qualifiedColumnName(String qualifier, String name) {
        return new QualifiedColumnName(qualifier, name);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("qualifier", qualifier)
                .append("name", name)
                .toString();
    }
}
