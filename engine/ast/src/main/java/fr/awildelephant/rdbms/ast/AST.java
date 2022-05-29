package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.Tree;

public interface AST extends Tree<AST> {

    <T> T accept(ASTVisitor<T> visitor);
}
