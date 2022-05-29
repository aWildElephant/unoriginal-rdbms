package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ColumnAlias extends UnaryNode<AST, AST> implements AST {

    private final String alias;

    public ColumnAlias(AST child, String alias) {
        super(child);
        this.alias = alias;
    }

    public static ColumnAlias columnAlias(AST child, String alias) {
        return new ColumnAlias(child, alias);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", child())
                .append("alias", alias)
                .toString();
    }

    public String alias() {
        return alias;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnAlias other)) {
            return false;
        }

        return Objects.equals(alias, other.alias)
                && equalsUnaryNode(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, child());
    }

}
