package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class UnqualifiedColumnName extends LeafNode<AST> implements ColumnName {

    private final String name;

    public UnqualifiedColumnName(String name) {
        this.name = name;
    }

    public static UnqualifiedColumnName unqualifiedColumnName(String name) {
        return new UnqualifiedColumnName(name);
    }

    public String name() {
        return name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("name", name)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnqualifiedColumnName other)) {
            return false;
        }

        return Objects.equals(name, other.name);
    }

}
