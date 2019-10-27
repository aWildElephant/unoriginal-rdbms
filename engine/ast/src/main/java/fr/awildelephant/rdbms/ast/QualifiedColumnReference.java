package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class QualifiedColumnReference implements ColumnReference {

    private final String qualifier;
    private final String name;

    private QualifiedColumnReference(String qualifier, String name) {
        this.qualifier = qualifier;
        this.name = name;
    }

    public static QualifiedColumnReference qualifiedColumnReference(String qualifier, String name) {
        return new QualifiedColumnReference(qualifier, name);
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
        if (!(obj instanceof QualifiedColumnReference)) {
            return false;
        }

        final QualifiedColumnReference other = (QualifiedColumnReference) obj;

        return Objects.equals(qualifier, other.qualifier)
                && Objects.equals(name, other.name);
    }
}
