package fr.awildelephant.rdbms.operator.logical.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ColumnAliasBuilder {

    private final Map<String, Map<String, String>> aliasing = new HashMap<>();

    public void add(ColumnReference original, String alias) {
        final String table = original.table().orElse("");
        final String column = original.name();

        aliasing.compute(column, (unused, aliases) -> {
            if (aliases == null) {
                aliases = new HashMap<>();
            }

            aliases.put(table, alias);

            return aliases;
        });
    }

    public Optional<ColumnAlias> build() {
        if (aliasing.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new ColumnAlias(aliasing));
        }
    }
}
