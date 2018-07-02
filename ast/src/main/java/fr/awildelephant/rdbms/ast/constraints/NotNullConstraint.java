package fr.awildelephant.rdbms.ast.constraints;

import java.util.Objects;

public final class NotNullConstraint {

    private final String columnName;

    public NotNullConstraint(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(columnName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NotNullConstraint)) {
            return false;
        }

        final NotNullConstraint other = (NotNullConstraint) obj;

        return Objects.equals(columnName, other.columnName);
    }

    public String columnName() {
        return columnName;
    }
}
