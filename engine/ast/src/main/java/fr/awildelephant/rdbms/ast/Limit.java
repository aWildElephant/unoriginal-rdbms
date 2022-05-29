package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Limit extends UnaryNode<AST, AST> implements AST {

    private final int limit;

    public Limit(AST child, int limit) {
        super(child);
        this.limit = limit;
    }

    public static Limit limit(AST child, int limit) {
        return new Limit(child, limit);
    }

    public int limit() {
        return limit;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("child", child())
                .append("limit", limit)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(child(), limit);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Limit other)) {
            return false;
        }

        return limit == other.limit
                && equalsUnaryNode(other);
    }
}
