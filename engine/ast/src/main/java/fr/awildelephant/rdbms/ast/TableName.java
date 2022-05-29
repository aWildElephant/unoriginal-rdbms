package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class TableName extends LeafNode<AST> implements AST {
    private final String name;

    public TableName(String name) {
        this.name = name;
    }

    public static TableName tableName(String name) {
        return new TableName(name);
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
                .append(name)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableName other)) {
            return false;
        }

        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
