package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class AliasCollector {

    private final List<AST> unaliasedColumns = new ArrayList<>();
    private final Map<String, Map<String, String>> aliasing = new HashMap<>();

    void addUnaliasedColumn(AST column) {
        unaliasedColumns.add(column);
    }

    List<AST> unaliasedColumns() {
        return unaliasedColumns;
    }

    void addAlias(String table, String original, String alias) {
        aliasing.compute(original, (unused, aliases) -> {
            if (aliases == null) {
                aliases = new HashMap<>();
            }

            aliases.put(table, alias);

            return aliases;
        });
    }

    Map<String, Map<String, String>> aliasing() {
        return aliasing;
    }
}
