package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;

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
}
