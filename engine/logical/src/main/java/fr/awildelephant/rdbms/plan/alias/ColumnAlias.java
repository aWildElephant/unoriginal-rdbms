package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Map;

public final class ColumnAlias implements Alias {

    private final Map<String, String> aliases;

    private ColumnAlias(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    public static ColumnAlias columnAlias(Map<String, String> aliases) {
        return new ColumnAlias(aliases);
    }

    @Override
    public ColumnReference alias(ColumnReference original) {
        final String columnName = original.name();
        final String aliasedColumnName = aliases.get(columnName);

        if (aliasedColumnName == null) {
            return original;
        }

        return original.renameColumn(aliasedColumnName);
    }

    @Override
    public ColumnReference unalias(ColumnReference aliased) {
        final String columnName = aliased.name();

        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (columnName.equals(entry.getValue())) {
                return aliased.renameColumn(entry.getValue());
            }
        }

        return aliased;
    }
}
