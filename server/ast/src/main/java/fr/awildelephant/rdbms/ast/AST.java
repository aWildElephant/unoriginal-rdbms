package fr.awildelephant.rdbms.ast;

public interface AST {

    <T> T accept(ASTVisitor<T> visitor);
}
