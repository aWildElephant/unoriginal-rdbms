package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

public class LongLiteral extends LeafNode<AST> implements AST {

    private final long value;

    private LongLiteral(long value) {
        this.value = value;
    }

    public static LongLiteral longLiteral(long value) {
        return new LongLiteral(value);
    }

    public long value() {
        return value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final LongLiteral other)) {
            return false;
        }

        return value == other.value;
    }
}
