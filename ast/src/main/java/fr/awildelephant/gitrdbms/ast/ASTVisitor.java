package fr.awildelephant.gitrdbms.ast;

public interface ASTVisitor<T> {

    T visit(ColumnDefinition columnDefinition);

    T visit(ColumnName columnName);

    T visit(CreateTable createTable);

    T visit(InsertInto insertInto);

    T visit(Row row);

    T visit(Rows rows);

    T visit(Select select);

    T visit(TableElementList tableElementList);

    T visit(TableName tableName);
}
