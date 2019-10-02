package fr.awildelephant.rdbms.schema;

import java.util.Objects;
import java.util.Optional;

public final class QualifiedColumnReference implements ColumnReference {

    private final String qualifier;
    private final String name;

    public QualifiedColumnReference(String qualifier, String name) {
        this.qualifier = qualifier;
        this.name = name;
    }

    @Override
    public Optional<String> qualifier() {
        return Optional.of(qualifier);
    }

    @Override
    public String name() {
        return name;
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
