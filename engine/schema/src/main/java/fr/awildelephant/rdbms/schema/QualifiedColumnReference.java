package fr.awildelephant.rdbms.schema;

import java.util.Objects;
import java.util.Optional;

public final class QualifiedColumnReference implements ColumnReference {

    private final String table;
    private final String name;

    public QualifiedColumnReference(String table, String name) {
        this.table = table;
        this.name = name;
    }

    @Override
    public Optional<String> table() {
        return Optional.of(table);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String fullName() {
        return table + '.' + name;
    }

    @Override
    public ColumnReference renameColumn(String newColumnName) {
        return new QualifiedColumnReference(table, newColumnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof QualifiedColumnReference)) {
            return false;
        }

        final QualifiedColumnReference other = (QualifiedColumnReference) obj;

        return Objects.equals(table, other.table)
                && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return table + '.' + name;
    }
}
