package fr.awildelephant.gitrdbms.parser.ast;

public interface ASTVisitor<T> {
    T visit(CreateTable createTable);
}
