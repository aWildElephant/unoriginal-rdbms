package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.ast.ColumnReference;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.CollectLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.algebraizer.ASTToValueExpressionTransformer.createValueExpression;
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
    private final SortSpecificationList sorting;

    private OutputColumnsTransformer(final LogicalOperator input, final List<? extends AST> outputColumns, final SortSpecificationList sorting) {
        this.input = input;
        this.outputColumns = new ArrayList<>(outputColumns);
        this.sorting = sorting;
        this.columnNameResolver = new ColumnNameResolver();
    }

    static LogicalOperator transformOutputColumns(final LogicalOperator input, final List<? extends AST> outputColumns, final SortSpecificationList sorting) {
        return new OutputColumnsTransformer(input, outputColumns, sorting).transform();
    }

    private LogicalOperator transform() {
        validateColumnReferences();

        final Map<String, String> aliasing = extractAliases();

        expandAsterisks();

        final List<String> outputColumnNames = collectProjectedColumnNames();

        final AggregationsExtractor aggregateExtractor = aggregationsExtractor(columnNameResolver);

        final ArrayList<AST> mapsBelowAggregates = new ArrayList<>();
        final List<Aggregate> aggregates = new ArrayList<>();
        final List<AST> mapsOverAggregates = new ArrayList<>();

        for (AST column : outputColumns) {
            final AST aggregateFreeOutputColumn = aggregateExtractor.apply(column);

            if (isFormula(aggregateFreeOutputColumn)) {
                mapsOverAggregates.add(aggregateFreeOutputColumn);
            }
        }

        for (AST aggregate : aggregateExtractor.collectedAggregates()) {
            if (aggregate instanceof CountStar) {
                aggregates.add(new CountStarAggregate());
            } else if (aggregate instanceof Avg) {
                final AST avgInput = ((Avg) aggregate).input();

                if (!(avgInput instanceof ColumnReference)) {
                    mapsBelowAggregates.add(avgInput);
                }

                aggregates.add(new AvgAggregate(columnNameResolver.apply(avgInput)));
            } else if (aggregate instanceof Min) {
                final AST minInput = ((Min) aggregate).input();

                if (!(minInput instanceof ColumnReference)) {
                    mapsBelowAggregates.add(minInput);
                }

                aggregates.add(new MinAggregate(columnNameResolver.apply(minInput)));
            } else if (aggregate instanceof Sum) {
                final AST sumInput = ((Sum) aggregate).input();

                if (!(sumInput instanceof ColumnReference)) {
                    mapsBelowAggregates.add(sumInput);
                }

                aggregates.add(new SumAggregate(columnNameResolver.apply(sumInput)));
            } else {
                throw new UnsupportedOperationException();
            }
        }

        if (!mapsBelowAggregates.isEmpty()) {
            final List<ValueExpression> valueExpressions = mapsBelowAggregates
                    .stream()
                    .map(map -> createValueExpression(map, input.schema()))
                    .collect(toList());

            final List<String> outputNames = mapsBelowAggregates.stream().map(columnNameResolver).collect(toList());

            input = new MapLop(input, valueExpressions, outputNames);
        }

        if (!aggregates.isEmpty()) {
            input = new AggregationLop(aggregates, input);
        }

        if (!mapsOverAggregates.isEmpty()) {
            final List<ValueExpression> valueExpressions = mapsOverAggregates
                    .stream()
                    .map(map -> createValueExpression(map, input.schema()))
                    .collect(toList());

            final List<String> outputNames = mapsOverAggregates.stream().map(columnNameResolver).collect(toList());

            input = new MapLop(input, valueExpressions, outputNames);
        }

        input = new ProjectionLop(input, outputColumnNames);

        if (!aliasing.isEmpty()) {
            input = aliasOperator(alias(aliasing), input);
        }

        if (sorting != null) {
            if (!aggregates.isEmpty()) {
                input = new CollectLop(input);
            }

            input = new SortLop(input, sorting.columns());
        }

        return input;
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
