package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LeftJoinLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SubqueryExecutionLop;
import fr.awildelephant.rdbms.plan.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.plan.alias.TableAlias;
import fr.awildelephant.rdbms.plan.arithmetic.AndExpression;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.awildelephant.rdbms.engine.optimizer.optimization.BreakdownFinder.canAddBreakdownOver;
import static fr.awildelephant.rdbms.engine.optimizer.optimization.CorrelatedFilterMatcher.isCorrelated;
import static fr.awildelephant.rdbms.plan.alias.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.plan.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterCollapser.collapseFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.Variable.variable;
import static java.util.stream.Collectors.toList;

public final class SubqueryDecorrelator extends DefaultLopVisitor<LogicalOperator> {

    private Set<Correlation> correlations = new HashSet<>();

    public static LogicalOperator decorrelateSubquery(LogicalOperator input, LogicalOperator subquery) {
        final SubqueryDecorrelator decorrelator = new SubqueryDecorrelator();

        final LogicalOperator transformedSubquery = decorrelator.apply(subquery);

        final Set<Correlation> correlation = decorrelator.correlations;
        if (!correlation.isEmpty()) {
            return transformToLeftJoin(input, transformedSubquery, correlation);
        } else {
            return new SubqueryExecutionLop(input, subquery);
        }
    }

    private static LeftJoinLop transformToLeftJoin(LogicalOperator input, LogicalOperator transformedSubquery,
                                                    Set<Correlation> correlations) {
        final Schema inputSchema = input.schema();
        final Schema transformedSubquerySchema = transformedSubquery.schema();

        final ValueExpression joinSpecification = buildJoinSpecifications(transformedSubquerySchema, inputSchema, correlations);

        final Schema joinOutputSchema = joinOutputSchema(inputSchema, transformedSubquerySchema);

        return new LeftJoinLop(input, transformedSubquery, joinSpecification, joinOutputSchema);
    }

    private static ValueExpression buildJoinSpecifications(Schema transformedSubquerySchema,
                                                           Schema inputSchema,
                                                           Set<Correlation> correlations) {
        return correlations.stream()
                .<ValueExpression>map(correlation -> {
                    final ColumnMetadata innerColumn = transformedSubquerySchema.column(correlation.getInnerColumn());
                    final Variable innerVariable = variable(innerColumn.name(), innerColumn.domain());

                    final ColumnMetadata outerColumn = inputSchema.column(correlation.getOuterColumn());
                    final Variable outerVariable = variable(outerColumn.name(), outerColumn.domain());

                    return equalExpression(outerVariable, innerVariable);
                })
                .reduce(AndExpression::andExpression)
                .orElseThrow();
    }

    private static Schema joinOutputSchema(Schema leftSchema, Schema rightSchema) {
        return leftSchema
                .extend(rightSchema.columnNames().stream().map(rightSchema::column).collect(toList()));
    }

    @Override
    public LogicalOperator visit(AggregationLop aggregationNode) {
        final LogicalOperator transformedInput = apply(aggregationNode.input());

        final List<ColumnReference> breakdowns = aggregationNode.breakdowns();
        if (!correlations.isEmpty()) {
            final List<ColumnReference> breakdownsWithCorrelatedColumns = new ArrayList<>(breakdowns);
            for (Correlation correlation : correlations) {
                if (!breakdownsWithCorrelatedColumns.contains(correlation.getInnerColumn())) {
                    breakdownsWithCorrelatedColumns.add(correlation.getInnerColumn());
                }
            }

            return new AggregationLop(transformedInput, breakdownsWithCorrelatedColumns, aggregationNode.aggregates());
        }

        return new AggregationLop(transformedInput, breakdowns, aggregationNode.aggregates());
    }

    @Override
    public LogicalOperator visit(ProjectionLop projectionNode) {
        final LogicalOperator transformedInput = apply(projectionNode.input());

        if (correlations.isEmpty()) {
            return new ProjectionLop(transformedInput, projectionNode.outputColumns());
        }

        final List<ColumnReference> outputColumns = new ArrayList<>(projectionNode.outputColumns());

        correlations.forEach(correlation -> {
            if (!outputColumns.contains(correlation.getInnerColumn())) {
                outputColumns.add(correlation.getInnerColumn());
            }
        });

        return new ProjectionLop(transformedInput, outputColumns);
    }

    @Override
    public LogicalOperator visit(ScalarSubqueryLop scalarSubquery) {
        final LogicalOperator transformedInput = apply(scalarSubquery.input());

        if (correlations.isEmpty()) {
            return scalarSubquery;
        }

        final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();
        final ColumnReference scalarSubqueryColumn = scalarSubquery.input().schema().columnNames().get(0);
        columnAliasBuilder.add(scalarSubqueryColumn, "0");

        final AliasLop aliasSubqueryOutputColumn = new AliasLop(transformedInput, columnAliasBuilder.build().orElseThrow());

        final TableAlias tableAlias = tableAlias(scalarSubqueryColumn.table().orElse(null), scalarSubquery.id());

        correlations = correlations.stream()
                .map(correlation -> new Correlation(tableAlias.alias(correlation.getInnerColumn()),
                                                    correlation.getOuterColumn()))
                .collect(Collectors.toSet());

        return new AliasLop(aliasSubqueryOutputColumn, tableAlias);
    }

    @Override
    public LogicalOperator visit(FilterLop filter) {
        final List<ValueExpression> expressions = expandFilters(filter.filter());

        final List<ValueExpression> correlatedFilters = new ArrayList<>();
        final List<ValueExpression> uncorrelatedFilters = new ArrayList<>();
        for (ValueExpression expression : expressions) {
            if (isCorrelated(expression)) {
                correlatedFilters.add(expression);
            } else {
                uncorrelatedFilters.add(expression);
            }
        }

        if (correlatedFilters.isEmpty()) {
            return filter.transformInputs(this);
        }

        // TODO: I don't remember what "canAddBreakdownOver" is for
        if (!correlatedFilters.stream().allMatch(this::isEqualFilterBetweenTwoColumns)
                || !canAddBreakdownOver(filter.input())) {
            return filter.transformInputs(this);
        }

        for (ValueExpression correlatedFilter : correlatedFilters) {
            correlations.add(getCorrelation(correlatedFilter));
        }

        return createFilterAbove(filter.input(), uncorrelatedFilters);
    }

    private LogicalOperator createFilterAbove(LogicalOperator transformedInput,
                                              List<ValueExpression> uncorrelatedFilters) {
        final Optional<ValueExpression> uncorrelatedFilter = collapseFilters(uncorrelatedFilters);
        if (uncorrelatedFilter.isPresent()) {
            return new FilterLop(transformedInput, uncorrelatedFilter.get());
        } else {
            return transformedInput;
        }
    }

    private Correlation getCorrelation(ValueExpression expression) {
        final EqualExpression equalExpression = (EqualExpression) expression;
        if (equalExpression.left() instanceof OuterQueryVariable) {
            final ColumnReference outerColumn = ((OuterQueryVariable) equalExpression.left()).reference();
            final ColumnReference innerColumn = ((Variable) equalExpression.right()).reference();

            return new Correlation(innerColumn, outerColumn);
        } else {
            final ColumnReference outerColumn = ((OuterQueryVariable) equalExpression.right()).reference();
            final ColumnReference innerColumn = ((Variable) equalExpression.left()).reference();

            return new Correlation(innerColumn, outerColumn);
        }
    }

    private boolean isEqualFilterBetweenTwoColumns(ValueExpression expression) {
        if (!(expression instanceof EqualExpression)) {
            return false;
        }

        final EqualExpression equalExpression = (EqualExpression) expression;

        return (equalExpression.left() instanceof OuterQueryVariable && equalExpression.right() instanceof Variable) ||
                (equalExpression.left() instanceof Variable && equalExpression.right() instanceof OuterQueryVariable);
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
