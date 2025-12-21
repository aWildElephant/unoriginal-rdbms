package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.List;

public class Coalesce extends NAryNode<AST, AST> implements AST {

    private Coalesce(List<AST> arguments) {
        super(arguments);
    }

    public static Coalesce coalesce(List<AST> arguments) {
        return new Coalesce(arguments);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Coalesce[arguments=" + children() + ']';
    }
}
