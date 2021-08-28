package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.schema.ColumnReference;

final class AliasExtractor {

    private final ColumnReferenceTransformer columnReferenceTransformer;

    AliasExtractor(ColumnReferenceTransformer columnReferenceTransformer) {
        this.columnReferenceTransformer = columnReferenceTransformer;
    }

    void extractAlias(AST column, AliasCollector collector) {
        if (column instanceof final ColumnAlias aliasedColumn) {

            final AST unaliasedColumn = aliasedColumn.input();

            final ColumnReference columnReference = columnReferenceTransformer.apply(unaliasedColumn);

            collector.addAlias(columnReference, aliasedColumn.alias());

            collector.addUnaliasedColumn(unaliasedColumn);
        } else {
            collector.addUnaliasedColumn(column);
        }
    }
}
