package fr.awildelephant.rdbms.ast;

import java.util.function.Function;

public interface ASTVisitor<T> extends Function<AST, T> {

    default T apply(AST node) {
        return node.accept(this);
    }

    T visit(Asterisk asterisk);

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
