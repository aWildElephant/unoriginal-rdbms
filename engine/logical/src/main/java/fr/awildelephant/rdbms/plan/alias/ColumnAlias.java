package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ColumnAlias implements Alias {

    private final Map<String, Map<String, String>> aliases;

    private ColumnAlias(Map<String, Map<String, String>> aliases) {
        this.aliases = aliases;
    }

    public static ColumnAlias columnAlias(Map<String, Map<String, String>> aliases) {
        return new ColumnAlias(aliases);
    }

    @Override
    public ColumnReference alias(ColumnReference original) {
        final Map<String, String> tables = this.aliases.get(original.name());

        if (tables == null) {
            return original;
        }

        final Optional<String> table = original.table();
        if (table.isPresent()) {
            final String alias = tables.get(table.get());
            if (alias != null) {
                return original.renameColumn(alias);
            }
        }

        return original.renameColumn(tables.values().iterator().next());
    }

    @Override
    public ColumnReference unalias(ColumnReference aliased) {
        final String aliasedColumnName = aliased.name();

        for (Map.Entry<String, Map<String, String>> aliasesByOriginalColumnName : aliases.entrySet()) {
            for (Map.Entry<String, String> aliasByTable : aliasesByOriginalColumnName.getValue().entrySet()) {
                if (aliasedColumnName.equals(aliasByTable.getValue())) {
                    return aliased.renameColumn(aliasesByOriginalColumnName.getKey());
                }
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aliases);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnAlias)) {
            return false;
        }

        final ColumnAlias other = (ColumnAlias) obj;

        return Objects.equals(aliases, other.aliases);
    }
}
