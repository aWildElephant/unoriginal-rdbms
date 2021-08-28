package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ColumnAlias implements Alias {

    private final Map<String, Map<String, String>> aliases;

    ColumnAlias(Map<String, Map<String, String>> aliases) {
        this.aliases = aliases;
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
                    final String optionalOriginalTableName = aliasByTable.getKey();
                    final String originalColumnName = aliasesByOriginalColumnName.getKey();
                    if (optionalOriginalTableName.isEmpty()) {
                        return new UnqualifiedColumnReference(originalColumnName);
                    } else {
                        return new QualifiedColumnReference(optionalOriginalTableName, originalColumnName);
                    }
                }
            }
        }

        return aliased;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aliases);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ColumnAlias other)) {
            return false;
        }

        return Objects.equals(aliases, other.aliases);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("aliases", aliases)
                .build();
    }
}
