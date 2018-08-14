package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

final class OutputColumnsTransformer {

    private final ColumnNameResolver columnNameResolver;

    private List<AST> outputColumns;

    OutputColumnsTransformer(Schema inputSchema, List<? extends AST> outputColumns) {
        columnNameResolver = new ColumnNameResolver(inputSchema);

        this.outputColumns = new ArrayList<>(outputColumns);
    }

    Map<String, String> extractAliases() {
        final HashMap<String, String> aliasing = new HashMap<>();

        for (int i = 0; i < outputColumns.size(); i++) {
            final AST column = outputColumns.get(i);

            if (column instanceof ColumnAlias) {
                final ColumnAlias aliasedColumn = (ColumnAlias) column;
                final AST unaliasedColumn = aliasedColumn.input();
                final String unaliasedColumnName = columnNameResolver.apply(unaliasedColumn)
                                                                     .findFirst()
                                                                     .orElseThrow(IllegalStateException::new);

                aliasing.put(unaliasedColumnName, aliasedColumn.alias());

                outputColumns.set(i, unaliasedColumn);
            }
        }

        return aliasing;
    }

    List<String> collectProjectedColumnNames() {
        return outputColumns.stream()
                            .flatMap(columnNameResolver::apply)
                            .collect(toList());
    }
}
