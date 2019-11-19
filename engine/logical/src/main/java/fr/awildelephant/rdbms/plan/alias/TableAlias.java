package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;

import java.util.Objects;

public final class TableAlias implements Alias {

    private final String alias;

    private TableAlias(String alias) {
        this.alias = alias;
    }

    public static TableAlias tableAlias(String alias) {
        return new TableAlias(alias);
    }

    @Override
    public ColumnReference alias(ColumnReference reference) {
        return new QualifiedColumnReference(alias, reference.name());
    }

    @Override
    public ColumnReference unalias(ColumnReference reference) {
        throw new UnsupportedOperationException("Removing a table alias is not yet implemented");
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableAlias)) {
            return false;
        }

        final TableAlias other = (TableAlias) obj;

        return Objects.equals(alias, other.alias);
    }
}
