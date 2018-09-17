package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class CreateTable implements AST {

    private final TableName tableName;
    private final TableElementList columns;

    private CreateTable(TableName tableName, TableElementList columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public static CreateTable createTable(TableName tableName, TableElementList columns) {
        return new CreateTable(tableName, columns);
    }

    public TableName tableName() {
        return tableName;
    }

    public TableElementList tableElementList() {
        return columns;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, columns);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreateTable)) {
            return false;
        }

        final CreateTable other = (CreateTable) obj;

        return Objects.equals(tableName, other.tableName)
                && Objects.equals(columns, other.columns);
    }
}
