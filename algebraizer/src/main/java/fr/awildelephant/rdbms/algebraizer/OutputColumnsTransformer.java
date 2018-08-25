package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.MapNode;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.plan.ProjectionNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final List<String> outputColumnNames = collectProjectedColumnNames();

        final List<Formula> constantMaps = collectConstantMaps();

        if (!constantMaps.isEmpty()) {
            input = new MapNode(constantMaps, input);
        }

        final ProjectionNode projection = new ProjectionNode(outputColumnNames, input);

        if (!aliasing.isEmpty()) {
            return aliasOperator(alias(aliasing), projection);
        }

        return projection;
    }

    private Map<String, String> extractAliases() {
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

    private List<String> collectProjectedColumnNames() {
        return outputColumns.stream()
                            .flatMap(columnNameResolver::apply)
                            .collect(toList());
    }

    private List<Formula> collectConstantMaps() {
        final ArrayList<AST> outputColumnsWithoutMaps = new ArrayList<>();
        final ArrayList<Formula> maps = new ArrayList<>();

        for (AST column : outputColumns) {
            if (isFormula(column)) {
                maps.add(new Formula(column, columnNameResolver.apply(column).findAny().get()));
            } else {
                outputColumnsWithoutMaps.add(column);
            }
        }

        outputColumns = outputColumnsWithoutMaps;

        return maps;
    }
}
