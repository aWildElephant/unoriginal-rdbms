package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.CollectLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.awildelephant.rdbms.plan.arithmetic.FilterCollapser.collapseFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;
import static java.util.stream.Collectors.toList;

/**
 * Moves a filter node down its input node if possible.
 */
public class FilterPushDown implements LopVisitor<LogicalOperator> {

    private final Collection<ValueExpression> filters;

    public FilterPushDown() {
        this(Collections.emptyList());
    }

    private FilterPushDown(Collection<ValueExpression> filters) {
        this.filters = filters;
    }

    @Override
    public LogicalOperator visit(AggregationLop aggregationNode) {
        final List<ColumnReference> aggregatesColumns = aggregationNode.aggregates()
                                                                       .stream()
                                                                       .map(Aggregate::outputName)
                                                                       .collect(toList());

        final List<ValueExpression> filtersOnAggregates = new ArrayList<>();
        final List<ValueExpression> filtersOnInput = new ArrayList<>();

        for (ValueExpression filter : filters) {
            if (filter.variables().anyMatch(aggregatesColumns::contains)) {
                filtersOnAggregates.add(filter);
            } else {
                filtersOnInput.add(filter);
            }
        }

        return createFilterAbove(filtersOnAggregates, new AggregationLop(aggregationNode.aggregates(),
                                                                         new FilterPushDown(filtersOnInput)
                                                                                 .apply(aggregationNode.input())));
    }

    @Override
    public LogicalOperator visit(AliasLop alias) {
        final ExpressionUnaliaser unaliaser = new ExpressionUnaliaser(alias.alias());

        final List<ValueExpression> unaliasedFilters = filters.stream().map(unaliaser).collect(toList());

        return new AliasLop(new FilterPushDown(unaliasedFilters).apply(alias.input()), alias.alias());
    }

    @Override
    public LogicalOperator visit(BaseTableLop baseTable) {
        return createFilterAbove(filters, baseTable);
    }

    @Override
    public LogicalOperator visit(BreakdownLop breakdownNode) {
        return new BreakdownLop(apply(breakdownNode.input()), breakdownNode.breakdowns());
    }

    @Override
    public LogicalOperator visit(CollectLop collectNode) {
        return new CollectLop(apply(collectNode.input()));
    }

    @Override
    public LogicalOperator visit(DistinctLop distinctNode) {
        return new DistinctLop(apply(distinctNode.input()));
    }

    @Override
    public LogicalOperator visit(FilterLop filter) {
        final List<ValueExpression> allFilters = expandFilters(filter.filter());
        allFilters.addAll(filters);

        return new FilterPushDown(allFilters).apply(filter.input());
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoinLop) {
        return innerJoinLop; // TODO, not necessary for Q3
    }

    @Override
    public LogicalOperator visit(LimitLop limitLop) {
        return new LimitLop(apply(limitLop.input()), limitLop.limit());
    }

    @Override
    public LogicalOperator visit(MapLop mapNode) {
        final Schema schema = mapNode.schema();

        final List<ValueExpression> filtersOnMapColumns = new ArrayList<>();
        final List<ValueExpression> filtersOnInput = new ArrayList<>();

        for (ValueExpression filter : filters) {
            if (filter.variables().anyMatch(schema::contains)) {
                filtersOnMapColumns.add(filter);
            } else {
                filtersOnInput.add(filter);
            }
        }

        return createFilterAbove(filtersOnMapColumns,
                                 new MapLop(new FilterPushDown(filtersOnInput).apply(mapNode.input()),
                                            mapNode.expressions(), mapNode.expressionsOutputNames()
                                 ));
    }

    @Override
    public LogicalOperator visit(ProjectionLop projectionNode) {
        return new ProjectionLop(apply(projectionNode.input()), projectionNode.outputColumns());
    }

    @Override
    public LogicalOperator visit(ScalarSubqueryLop scalarSubquery) {
        final LogicalOperator transformedSubquery = new FilterPushDown().apply(scalarSubquery.input());

        return createFilterAbove(filters, new ScalarSubqueryLop(transformedSubquery, scalarSubquery.id()));
    }

    @Override
    public LogicalOperator visit(SortLop sortLop) {
        return new SortLop(apply(sortLop.input()), sortLop.sortSpecificationList());
    }

    @Override
    public LogicalOperator visit(TableConstructorLop tableConstructor) {
        return createFilterAbove(filters, tableConstructor);
    }

    private static LogicalOperator createFilterAbove(Iterable<ValueExpression> filters, LogicalOperator node) {
        final Optional<ValueExpression> filter = collapseFilters(filters);

        if (filter.isPresent()) {
            return new FilterLop(node, filter.get());
        }

        return node;
    }

    @Override
    public LogicalOperator visit(CartesianProductLop cartesianProduct) {
        final Collection<ValueExpression> aboveFilters = new ArrayList<>();
        final Collection<ValueExpression> leftFilters = new ArrayList<>();
        final Collection<ValueExpression> rightFilters = new ArrayList<>();
        final Collection<ValueExpression> joinFilters = new ArrayList<>();

        final Schema leftSchema = cartesianProduct.leftInput().schema();
        final Schema rightSchema = cartesianProduct.rightInput().schema();

        for (ValueExpression filter : filters) {
            final List<ColumnReference> requiredVariables = filter.variables().collect(toList());

            final boolean requiresLeftInput = requiredVariables.stream().anyMatch(leftSchema::contains);
            final boolean requiresRightInput = requiredVariables.stream().anyMatch(rightSchema::contains);

            if (!requiresLeftInput) {
                rightFilters.add(filter);
            }

            if (!requiresRightInput) {
                leftFilters.add(filter);
            }

            if (requiresLeftInput && requiresRightInput) {
                if (filter instanceof EqualExpression) {
                    joinFilters.add(filter);
                } else {
                    aboveFilters.add(filter);
                }
            }
        }

        final LogicalOperator leftInput = new FilterPushDown(leftFilters).apply(cartesianProduct.leftInput());
        final LogicalOperator rightInput = new FilterPushDown(rightFilters).apply(cartesianProduct.rightInput());

        final Optional<ValueExpression> collapsedJoinFilter = collapseFilters(joinFilters);

        final LogicalOperator joinNode;
        if (collapsedJoinFilter.isPresent()) {
            joinNode = new InnerJoinLop(leftInput, rightInput, collapsedJoinFilter.get(), cartesianProduct.schema());
        } else {
            joinNode = new CartesianProductLop(leftInput, rightInput, cartesianProduct.schema());
        }

        if (aboveFilters.isEmpty()) {
            return joinNode;
        }

        return createFilterAbove(aboveFilters, joinNode);
    }
}
