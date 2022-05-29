package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Explain extends UnaryNode<AST, AST> implements AST {

    public Explain(AST child) {
        super(child);
    }

    public static Explain explain(AST child) {
        return new Explain(child);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this).append(child()).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Explain other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
