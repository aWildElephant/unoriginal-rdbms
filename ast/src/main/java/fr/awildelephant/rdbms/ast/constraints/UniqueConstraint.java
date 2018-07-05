package fr.awildelephant.rdbms.ast.constraints;

import java.util.Objects;
import java.util.Set;

public final class UniqueConstraint {

    private final Set<String> columnNames;

    public UniqueConstraint(Set<String> columnNames) {
        this.columnNames = columnNames;
    }

    public Set<String> columnNames() {
        return columnNames;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(columnNames);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UniqueConstraint)) {
            return false;
        }

        final UniqueConstraint other = (UniqueConstraint) obj;

        return Objects.equals(columnNames, other.columnNames);
    }
}
