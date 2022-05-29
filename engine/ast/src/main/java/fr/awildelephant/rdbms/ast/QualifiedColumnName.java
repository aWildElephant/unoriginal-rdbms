package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class QualifiedColumnName extends LeafNode<AST> implements ColumnName {

    private final String qualifier;
    private final String name;

    public QualifiedColumnName(String qualifier, String name) {
        this.qualifier = qualifier;
        this.name = name;
    }

    public static QualifiedColumnName qualifiedColumnName(String qualifier, String name) {
        return new QualifiedColumnName(qualifier, name);
    }

    public String qualifier() {
        return qualifier;
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
                .append("qualifier", qualifier)
                .append("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof QualifiedColumnName other)) {
            return false;
        }

        return Objects.equals(qualifier, other.qualifier)
                && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifier, name);
    }
}
