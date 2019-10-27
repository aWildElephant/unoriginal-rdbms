package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class UnqualifiedColumnName implements ColumnName {

    private final String name;

    private UnqualifiedColumnName(String name) {
        this.name = name;
    }

    public static UnqualifiedColumnName unqualifiedColumnName(String name) {
        return new UnqualifiedColumnName(name);
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
        if (!(obj instanceof UnqualifiedColumnName)) {
            return false;
        }

        final UnqualifiedColumnName other = (UnqualifiedColumnName) obj;

        return Objects.equals(name, other.name);
    }
}
