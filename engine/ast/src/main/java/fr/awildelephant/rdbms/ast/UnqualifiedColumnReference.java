package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class UnqualifiedColumnReference implements ColumnReference {

    private final String name;

    private UnqualifiedColumnReference(String name) {
        this.name = name;
    }

    public static UnqualifiedColumnReference unqualifiedColumnReference(String name) {
        return new UnqualifiedColumnReference(name);
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
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnqualifiedColumnReference)) {
            return false;
        }

        final UnqualifiedColumnReference other = (UnqualifiedColumnReference) obj;

        return Objects.equals(name, other.name);
    }
}
