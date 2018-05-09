package fr.awildelephant.gitrdbms.parser.ast;

public interface AST {

    <T> T accept(ASTVisitor<T> visitor);
}
