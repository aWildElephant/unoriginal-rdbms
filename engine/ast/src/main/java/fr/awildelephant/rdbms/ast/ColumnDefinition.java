package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public record ColumnDefinition(String columnName, ColumnType columnType) implements AST {

    public static ColumnDefinition column(String name, ColumnType type) {
        return new ColumnDefinition(name, type);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
