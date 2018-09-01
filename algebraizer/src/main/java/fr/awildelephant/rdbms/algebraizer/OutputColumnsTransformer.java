package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.AggregationNode;
import fr.awildelephant.rdbms.plan.MapNode;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.plan.ProjectionNode;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.awildelephant.rdbms.algebraizer.ASTToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.algebraizer.FormulaOrNotFormulaDifferentiator.isFormula;
import static fr.awildelephant.rdbms.plan.AliasNode.aliasOperator;
import static fr.awildelephant.rdbms.schema.Alias.alias;
import static java.util.stream.Collectors.toList;

final class OutputColumnsTransformer {

    private final ColumnNameResolver columnNameResolver;

    private Plan input;
    private List<AST> outputColumns;

    private OutputColumnsTransformer(final Plan input, final List<? extends AST> outputColumns) {
        this.input = input;
        this.outputColumns = new ArrayList<>(outputColumns);
        this.columnNameResolver = new ColumnNameResolver(input.schema());
    }

    static Plan transformOutputColumns(final Plan input, final List<? extends AST> outputColumns) {
        return new OutputColumnsTransformer(input, outputColumns).transform();
    }

    private Plan transform() {
        final Map<String, String> aliasing = extractAliases();

        expandAsterisks();

        final List<String> outputColumnNames = collectProjectedColumnNames();

        final List<Formula> constantMaps = collectMaps();

        if (!constantMaps.isEmpty()) {
            input = new MapNode(constantMaps, input);
        }

        final List<Aggregate> aggregates = collectAggregates();

        if (!aggregates.isEmpty()) {
            input = new AggregationNode(input);
        }

        final ProjectionNode projection = new ProjectionNode(outputColumnNames, input);

        if (!aliasing.isEmpty()) {
            return aliasOperator(alias(aliasing), projection);
        }

        return projection;
    }

    private List<Aggregate> collectAggregates() {
        final List<AST> notAggregates = new ArrayList<>();
        final List<Aggregate> aggregates = new ArrayList<>();

        for (AST column : outputColumns) {
            if (column instanceof CountStar) {
                aggregates.add(new CountStarAggregate());
            } else {
                notAggregates.add(column);
            }
        }

        outputColumns = notAggregates;

        return aggregates;
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

    private List<Formula> collectMaps() {
        final List<AST> notMaps = new ArrayList<>();
        final List<Formula> maps = new ArrayList<>();

        for (AST column : outputColumns) {
            if (isFormula(column)) {
                maps.add(createFormula(column, input.schema(), columnNameResolver.apply(column)));
            } else {
                notMaps.add(column);
            }
        }

        outputColumns = notMaps;

        return maps;
    }
}
