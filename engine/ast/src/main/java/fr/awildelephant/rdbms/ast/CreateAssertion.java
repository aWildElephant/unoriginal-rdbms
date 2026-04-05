package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

public final class CreateAssertion extends UnaryNode<AST, AST> implements AST {

    private final String name;

    public CreateAssertion(String name, AST predicate) {
        super(predicate);
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
