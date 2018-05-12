package fr.awildelephant.gitrdbms.ast;

public interface AST {

    <T> T accept(ASTVisitor<T> visitor);
}
