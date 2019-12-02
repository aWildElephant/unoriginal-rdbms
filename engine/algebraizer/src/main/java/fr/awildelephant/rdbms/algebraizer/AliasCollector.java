package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.plan.alias.ColumnAlias;
import fr.awildelephant.rdbms.plan.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class AliasCollector {

    private final List<AST> unaliasedColumns = new ArrayList<>();
    private final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();

    void addUnaliasedColumn(AST column) {
        unaliasedColumns.add(column);
    }

    List<AST> unaliasedColumns() {
        return unaliasedColumns;
    }

    void addAlias(ColumnReference original, String alias) {
        columnAliasBuilder.add(original, alias);
    }

    Optional<ColumnAlias> aliasing() {
        return columnAliasBuilder.build();
    }
}
