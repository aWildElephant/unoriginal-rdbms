package fr.awildelephant.rdbms.ast.constraints;

import java.util.Objects;
import java.util.Set;

public final class ForeignKeyConstraint {

    private final String targetTable;
    private final Set<String> columns;
    private final Set<String> targetColumns;

    public ForeignKeyConstraint(String targetTable, Set<String> columns, Set<String> targetColumns) {
        this.targetTable = targetTable;
        this.columns = columns;
        this.targetColumns = targetColumns;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetTable, columns, targetColumns);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ForeignKeyConstraint)) {
            return false;
        }

        final ForeignKeyConstraint other = (ForeignKeyConstraint) obj;

        return Objects.equals(targetTable, other.targetTable)
                && Objects.equals(columns, other.columns)
                && Objects.equals(targetColumns, other.targetColumns);
    }
}
