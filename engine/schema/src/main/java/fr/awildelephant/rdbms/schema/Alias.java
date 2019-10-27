package fr.awildelephant.rdbms.schema;

import java.util.Map;

public final class Alias {

    private final Map<String, String> aliases;

    private Alias(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    public static Alias alias(Map<String, String> aliases) {
        return new Alias(aliases);
    }

    public ColumnReference get(ColumnReference original) {
        final String columnName = original.name();
        final String aliasedColumnName = aliases.get(columnName);

        if (aliasedColumnName == null) {
            return original;
        }

        return original.renameColumn(aliasedColumnName);
    }

    public ColumnReference revert(ColumnReference aliased) {
        final String columnName = aliased.name();

        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (columnName.equals(entry.getValue())) {
                return aliased.renameColumn(entry.getValue());
            }
        }

        return aliased;
    }
}
