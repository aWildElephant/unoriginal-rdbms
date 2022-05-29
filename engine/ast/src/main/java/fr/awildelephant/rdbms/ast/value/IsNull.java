package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class IsNull extends UnaryNode<AST, AST> implements AST {

    public IsNull(AST child) {
        super(child);
    }

    public static IsNull isNull(AST child) {
        return new IsNull(child);
    }

    public AST input() {
        return child();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(child())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final IsNull other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
