package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.algebraizer.ASTToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.algebraizer.AggregationsExtractor.aggregationsExtractor;
import static fr.awildelephant.rdbms.algebraizer.FormulaOrNotFormulaDifferentiator.isFormula;
import static fr.awildelephant.rdbms.algebraizer.SchemaValidator.schemaValidator;
import static fr.awildelephant.rdbms.plan.AliasLop.aliasOperator;
import static fr.awildelephant.rdbms.schema.Alias.alias;
import static java.util.stream.Collectors.toList;

final class OutputColumnsTransformer {

    private final ColumnNameResolver columnNameResolver;

    private LogicalOperator input;
    private List<AST> outputColumns;

    private OutputColumnsTransformer(final LogicalOperator input, final List<? extends AST> outputColumns) {
        this.input = input;
        this.outputColumns = new ArrayList<>(outputColumns);
        this.columnNameResolver = new ColumnNameResolver();
    }

    static LogicalOperator transformOutputColumns(final LogicalOperator input, final List<? extends AST> outputColumns) {
        return new OutputColumnsTransformer(input, outputColumns).transform();
    }

    private LogicalOperator transform() {
        validateColumnReferences();

        final Map<String, String> aliasing = extractAliases();

        expandAsterisks();

        final List<String> outputColumnNames = collectProjectedColumnNames();

        final AggregationsExtractor aggregateExtractor = aggregationsExtractor(columnNameResolver);

        final List<AST> mapsOverAggregates = new ArrayList<>();
        final List<Aggregate> aggregates = new ArrayList<>();

        for (AST column : outputColumns) {
            final AST aggregateFreeOutputColumn = aggregateExtractor.apply(column);

            if (isFormula(aggregateFreeOutputColumn)) {
                mapsOverAggregates.add(aggregateFreeOutputColumn);
            }

            for (AST ignored : aggregateExtractor.collectedAggregates()) {
                aggregates.add(new CountStarAggregate());
            }
        }

        if (!aggregates.isEmpty()) {
            input = new AggregationLop(input);
        }

        if (!mapsOverAggregates.isEmpty()) {
            input = new MapLop(mapsOverAggregates.stream()
                                                 .map(map -> createFormula(map, input.schema(), columnNameResolver.apply(map)))
                                                 .collect(toList()),
                               input);
        }

        final ProjectionLop projection = new ProjectionLop(outputColumnNames, input);

        if (!aliasing.isEmpty()) {
            return aliasOperator(alias(aliasing), projection);
        }

        return projection;
    }

    private void validateColumnReferences() {
        final SchemaValidator validator = schemaValidator(input.schema());

        outputColumns.forEach(validator::apply);
    }

    private Map<String, String> extractAliases() {
        final Map<String, String> aliasing = new HashMap<>();

        for (int i = 0; i < outputColumns.size(); i++) {
            final AST column = outputColumns.get(i);

            if (column instanceof ColumnAlias) {
                final ColumnAlias aliasedColumn = (ColumnAlias) column;
                final AST unaliasedColumn = aliasedColumn.input();
                final String unaliasedColumnName = columnNameResolver.apply(unaliasedColumn);

                aliasing.put(unaliasedColumnName, aliasedColumn.alias());

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

    private List<String> collectProjectedColumnNames() {
        return outputColumns.stream()
                            .map(columnNameResolver)
                            .collect(toList());
    }
}
