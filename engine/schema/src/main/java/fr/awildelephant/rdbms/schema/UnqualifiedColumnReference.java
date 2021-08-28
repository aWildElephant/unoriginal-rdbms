package fr.awildelephant.rdbms.schema;

import java.util.Objects;
import java.util.Optional;

public final class UnqualifiedColumnReference implements ColumnReference {

    private final String name;

    public UnqualifiedColumnReference(String name) {
        this.name = name;
    }

    @Override
    public Optional<String> table() {
        return Optional.empty();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String fullName() {
        return name;
    }

    @Override
    public ColumnReference renameColumn(String newColumnName) {
        return new UnqualifiedColumnReference(newColumnName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final UnqualifiedColumnReference other)) {
            return false;
        }

        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
