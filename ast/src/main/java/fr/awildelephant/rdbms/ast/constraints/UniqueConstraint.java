package fr.awildelephant.rdbms.ast.constraints;

import java.util.Objects;

public final class UniqueConstraint {

    private final String columnName;

    public UniqueConstraint(String columnName) {
        this.columnName = columnName;
    }

    public String columnName() {
        return columnName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(columnName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UniqueConstraint)) {
            return false;
        }

        final UniqueConstraint other = (UniqueConstraint) obj;

        return Objects.equals(columnName, other.columnName);
    }
}
