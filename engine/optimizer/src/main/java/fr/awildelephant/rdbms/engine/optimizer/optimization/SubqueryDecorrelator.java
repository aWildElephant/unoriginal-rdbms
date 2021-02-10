package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.*;
import fr.awildelephant.rdbms.plan.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.plan.alias.TableAlias;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.awildelephant.rdbms.engine.optimizer.optimization.BreakdownFinder.canAddBreakdownOver;
import static fr.awildelephant.rdbms.engine.optimizer.optimization.CorrelatedFilterMatcher.isCorrelated;
import static fr.awildelephant.rdbms.plan.alias.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.plan.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterCollapser.collapseFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.Variable.variable;
import static java.util.stream.Collectors.toList;

public final class SubqueryDecorrelator extends DefaultLopVisitor<LogicalOperator> {

    private Correlation correlation;

    public static LogicalOperator decorrelateSubquery(LogicalOperator input, LogicalOperator subquery) {
        final SubqueryDecorrelator decorrelator = new SubqueryDecorrelator();

        final LogicalOperator transformedSubquery = decorrelator.apply(subquery);

        final Correlation correlation = decorrelator.correlation;
        if (correlation != null) {
            return transformToInnerJoin(input, transformedSubquery, correlation);
        } else {
            return new SubqueryExecutionLop(input, transformedSubquery);
        }
    }

    private static InnerJoinLop transformToInnerJoin(LogicalOperator input, LogicalOperator transformedSubquery,
                                                     Correlation correlation) {
        final Schema inputSchema = input.schema();
        final Schema transformedSubquerySchema = transformedSubquery.schema();

        final ColumnMetadata innerColumn = transformedSubquerySchema.column(correlation.getInnerColumn());
        final Variable innerVariable = variable(innerColumn.name(), innerColumn.domain());

        final ColumnMetadata outerColumn = inputSchema.column(correlation.getOuterColumn());
        final Variable outerVariable = variable(outerColumn.name(), outerColumn.domain());

        final EqualExpression joinSpecification = equalExpression(innerVariable, outerVariable);
        final Schema joinOutputSchema = joinOutputSchema(inputSchema, transformedSubquerySchema);

        return new InnerJoinLop(input, transformedSubquery, joinSpecification, joinOutputSchema);
    }

    private static Schema joinOutputSchema(Schema leftSchema, Schema rightSchema) {
        return leftSchema
                .extend(rightSchema.columnNames().stream().map(rightSchema::column).collect(toList()));
    }

    @Override
    public LogicalOperator visit(AggregationLop aggregationNode) {
        final LogicalOperator transformedInput = apply(aggregationNode.input());

        final List<ColumnReference> breakdowns = aggregationNode.breakdowns();
        if (correlation != null && !breakdowns.contains(correlation.getInnerColumn())) {
            final List<ColumnReference> breakdownsWithCorrelatedColumn = new ArrayList<>(breakdowns.size() + 1);
            breakdownsWithCorrelatedColumn.addAll(breakdowns);
            breakdownsWithCorrelatedColumn.add(correlation.getInnerColumn());

            return new AggregationLop(transformedInput, breakdownsWithCorrelatedColumn, aggregationNode.aggregates());
        }

        return new AggregationLop(transformedInput, breakdowns, aggregationNode.aggregates());
    }

    @Override
    public LogicalOperator visit(ProjectionLop projectionNode) {
        final LogicalOperator transformedInput = apply(projectionNode.input());

        if (correlation == null) {
            return new ProjectionLop(transformedInput, projectionNode.outputColumns());
        }

        final List<ColumnReference> outputColumns = new ArrayList<>(projectionNode.outputColumns());

        if (!outputColumns.contains(correlation.getInnerColumn())) {
            outputColumns.add(correlation.getInnerColumn());
        }

        return new ProjectionLop(transformedInput, outputColumns);
    }

    @Override
    public LogicalOperator visit(ScalarSubqueryLop scalarSubquery) {
        final LogicalOperator transformedInput = apply(scalarSubquery.input());

        if (correlation == null) {
            return scalarSubquery;
        }

        final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();
        columnAliasBuilder.add(scalarSubquery.input().schema().columnNames().get(0), "0");

        final AliasLop aliasSubqueryOutputColumn = new AliasLop(transformedInput, columnAliasBuilder.build().orElseThrow());

        final TableAlias tableAlias = tableAlias(scalarSubquery.id());

        correlation = new Correlation(tableAlias.alias(correlation.getInnerColumn()), correlation.getOuterColumn());

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

        // TODO: we don't need to support several correlated filters so we don't
        final int numberOfCorrelatedFilters = correlatedFilters.size();
        if (numberOfCorrelatedFilters != 1
                || !isEqualFilterBetweenTwoColumns(correlatedFilters.get(0))
                || !canAddBreakdownOver(filter.input())) {
            return filter.transformInputs(this);
        }

        correlation = getCorrelation(correlatedFilters.get(0), filter.input().schema());

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

    private Correlation getCorrelation(ValueExpression expression, Schema inputSchema) {
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
