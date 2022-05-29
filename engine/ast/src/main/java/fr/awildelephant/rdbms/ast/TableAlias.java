package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class TableAlias extends UnaryNode<AST, AST> implements AST {

    private final String alias;

    public TableAlias(AST child, String alias) {
        super(child);
        this.alias = alias;
    }

    public static TableAlias tableAlias(AST input, String alias) {
        return new TableAlias(input, alias);
    }

    public String alias() {
        return alias;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("child", child())
                .append("alias", alias)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(child(), alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableAlias other)) {
            return false;
        }

        return Objects.equals(alias, other.alias)
                && equalsUnaryNode(other);
    }

}
