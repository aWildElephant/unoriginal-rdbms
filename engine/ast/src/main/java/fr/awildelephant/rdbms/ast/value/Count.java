package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

public final class Count extends UnaryNode<AST, AST> implements AST {

    private final boolean distinct;

    private Count(boolean distinct, AST child) {
        super(child);
        this.distinct = distinct;
    }

    public static Count count(boolean distinct, AST child) {
        return new Count(distinct, child);
    }

    public boolean distinct() {
        return distinct;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct, child());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Count other)) {
            return false;
        }

        return equalsUnaryNode(other);
    }
}
