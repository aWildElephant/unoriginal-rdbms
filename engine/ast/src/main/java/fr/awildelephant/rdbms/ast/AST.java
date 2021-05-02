package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

public interface AST {

    <T> T accept(ASTVisitor<T> visitor);
}
