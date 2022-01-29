package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public record CreateTable(TableName tableName, TableElementList columns) implements AST {

    public static CreateTable createTable(TableName tableName, TableElementList columns) {
        return new CreateTable(tableName, columns);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
