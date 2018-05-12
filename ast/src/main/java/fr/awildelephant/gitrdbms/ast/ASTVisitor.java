package fr.awildelephant.gitrdbms.ast;

public interface ASTVisitor<T> {

    T visit(CreateTable createTable);

    T visit(ColumnDefinition columnDefinition);
}
