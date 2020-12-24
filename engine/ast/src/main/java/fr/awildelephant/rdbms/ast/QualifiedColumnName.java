package fr.awildelephant.rdbms.ast;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class QualifiedColumnName implements ColumnName {

    private final String qualifier;
    private final String name;

    private QualifiedColumnName(String qualifier, String name) {
        this.qualifier = qualifier;
        this.name = name;
    }

    public static QualifiedColumnName qualifiedColumnName(String qualifier, String name) {
        return new QualifiedColumnName(qualifier, name);
    }

    public String qualifier() {
        return qualifier;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifier, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof QualifiedColumnName)) {
            return false;
        }

        final QualifiedColumnName other = (QualifiedColumnName) obj;

        return Objects.equals(qualifier, other.qualifier)
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("qualifier", qualifier)
                .append("name", name)
                .toString();
    }
}
