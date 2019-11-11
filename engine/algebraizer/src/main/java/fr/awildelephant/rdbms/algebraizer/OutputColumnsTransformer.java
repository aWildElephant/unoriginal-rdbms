package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.algebraizer.SchemaValidator.schemaValidator;
import static fr.awildelephant.rdbms.plan.alias.ColumnAlias.columnAlias;
import static java.util.stream.Collectors.toList;

final class OutputColumnsTransformer {

    private final ColumnReferenceTransformer columnReferenceTransformer;
    private final ValuesTransformer valuesTransformer;

    private LogicalOperator input;
    private List<AST> outputColumns;
    private final SortSpecificationList sorting;

    private OutputColumnsTransformer(final LogicalOperator input, final List<? extends AST> outputColumns, final SortSpecificationList sorting) {
        this.input = input;
        this.outputColumns = new ArrayList<>(outputColumns);
        this.sorting = sorting;

        final ColumnNameResolver columnNameResolver = new ColumnNameResolver();
        this.columnReferenceTransformer = new ColumnReferenceTransformer(columnNameResolver);
        this.valuesTransformer = new ValuesTransformer(columnNameResolver, columnReferenceTransformer);
    }

    static LogicalOperator transformOutputColumns(final LogicalOperator input, final List<? extends AST> outputColumns, final SortSpecificationList sorting) {
        return new OutputColumnsTransformer(input, outputColumns, sorting).transform();
    }

    private LogicalOperator transform() {
        validateColumnReferences();

        final Map<String, Map<String, String>> aliasing = extractAliases();

        expandAsterisks();

        final List<ColumnReference> outputColumnReferences = collectProjectedColumnNames();

        input = valuesTransformer.transform(input, outputColumns);

        input = new ProjectionLop(input, outputColumnReferences);

        if (!aliasing.isEmpty()) {
            input = new AliasLop(input, columnAlias(aliasing));
        }

        if (sorting != null) {
            input = new SortLop(input, sorting.columns());
        }

        return input;
    }

    private void validateColumnReferences() {
        final SchemaValidator validator = schemaValidator(input.schema());

        outputColumns.forEach(validator::apply);
    }

    private Map<String, Map<String, String>> extractAliases() {
        final Map<String, Map<String, String>> aliasing = new HashMap<>();

        for (int i = 0; i < outputColumns.size(); i++) {
            final AST column = outputColumns.get(i);

            if (column instanceof ColumnAlias) {
                final ColumnAlias aliasedColumn = (ColumnAlias) column;
                final AST unaliasedColumn = aliasedColumn.input();
                final ColumnReference unaliasedColumnReference = columnReferenceTransformer.apply(unaliasedColumn);

                aliasing.compute(unaliasedColumnReference.name(), (unused, aliases) -> {
                    if (aliases == null) {
                        aliases = new HashMap<>();
                    }

                    aliases.put(unaliasedColumnReference.table().orElse(""), aliasedColumn.alias());

                    return aliases;
                });

                outputColumns.set(i, unaliasedColumn);
            }
        }

        return aliasing;
    }

    private void expandAsterisks() {
        final AsteriskExpander expander = new AsteriskExpander(input.schema());

        outputColumns = outputColumns.stream()
                                     .flatMap(expander)
                                     .collect(toList());
    }

    private List<ColumnReference> collectProjectedColumnNames() {
        final List<ColumnReference> outputColumnsReferences = new ArrayList<>(outputColumns.size());

        for (AST column : outputColumns) {
            outputColumnsReferences.add(columnReferenceTransformer.apply(column));
        }

        return outputColumnsReferences;
    }
}
