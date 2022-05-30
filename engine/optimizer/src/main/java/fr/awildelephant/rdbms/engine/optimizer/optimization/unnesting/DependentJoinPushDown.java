package fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting;

import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.DependentJoinLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.function.VariableCollector;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.engine.optimizer.util.AttributesFunction.attributes;
import static fr.awildelephant.rdbms.engine.optimizer.util.FreeVariablesFunction.freeVariables;
import static fr.awildelephant.rdbms.engine.optimizer.util.SetHelper.intersection;
import static fr.awildelephant.rdbms.plan.filter.FilterCollapser.collapseFilters;
import static fr.awildelephant.rdbms.plan.filter.FilterExpander.expandFilters;
import static java.util.stream.Collectors.toSet;

public final class DependentJoinPushDown extends DefaultLopVisitor<LogicalOperator> {

    private final VariableCollector variableCollector;

    public DependentJoinPushDown(VariableCollector variableCollector) {
        this.variableCollector = variableCollector;
    }

    @Override
    public LogicalOperator visit(DependentJoinLop dependentJoin) {
        final LogicalOperator leftInput = dependentJoin.left();
        final LogicalOperator rightInput = dependentJoin.right();

        if (intersection(freeVariables(rightInput), attributes(leftInput)).isEmpty()) {
            // TODO: is a cartesian product ok here ? (what about the dependent join predicate)
            return new CartesianProductLop(leftInput, rightInput, dependentJoin.schema());
        } else {
            return new DependentJoinPushDownRules(leftInput, dependentJoin.predicate()).apply(rightInput);
        }
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }

    public final class DependentJoinPushDownRules extends DefaultLopVisitor<LogicalOperator> {

        // d must be a set (no duplicate). This is ensured by the dependent join rewriting before the push down
        private final LogicalOperator d;
        private final Set<ColumnReference> attributesD;
        private final ValueExpression predicate;

        public DependentJoinPushDownRules(LogicalOperator d,
                                          ValueExpression predicate) {
            this.d = d;
            this.attributesD = attributes(d);
            this.predicate = predicate;
        }

        @Override
        public LogicalOperator visit(AggregationLop aggregation) {
            final List<ColumnReference> newBreakdowns = new ArrayList<>(aggregation.breakdowns());
            for (ColumnReference newBreakdown : attributesD) {
                if (!newBreakdowns.contains(newBreakdown)) {
                    newBreakdowns.add(newBreakdown);
                }
            }

            final Set<ColumnReference> aggregateOutputs = aggregation.aggregates().stream()
                    .map(Aggregate::outputColumn)
                    .collect(toSet());

            final List<ValueExpression> expandedFilters = expandPredicate();
            final List<ValueExpression> filtersAbove = new ArrayList<>();
            final List<ValueExpression> filtersBelow = new ArrayList<>();
            for (ValueExpression filter : expandedFilters) {
                if (variables(filter).anyMatch(aggregateOutputs::contains)) {
                    filtersAbove.add(filter);
                } else {
                    filtersBelow.add(filter);
                }
            }

            final ValueExpression collapsedFilterBelow = collapseFilters(filtersBelow).orElse(null);
            final LogicalOperator transformedInput = new DependentJoinPushDownRules(d, collapsedFilterBelow)
                    .apply(aggregation.input());

            final AggregationLop transformedNode = new AggregationLop(transformedInput,
                    newBreakdowns,
                    aggregation.aggregates());

            return createFilterAbove(transformedNode, filtersAbove);
        }

        private List<ValueExpression> expandPredicate() {
            if (predicate == null) {
                return List.of();
            }

            return expandFilters(predicate);
        }

        private LogicalOperator createFilterAbove(LogicalOperator node, List<ValueExpression> filters) {
            final Optional<ValueExpression> filter = collapseFilters(filters);

            return filter.<LogicalOperator>map(f -> new FilterLop(node, f)).orElse(node);
        }

        @Override
        public LogicalOperator visit(CartesianProductLop cartesianProduct) {
            final LogicalOperator left = cartesianProduct.left();
            final LogicalOperator right = cartesianProduct.right();

            if (intersection(freeVariables(left), attributesD).isEmpty()) {
                return new CartesianProductLop(left, apply(right));
            } else if (intersection(freeVariables(right), attributesD).isEmpty()) {
                return new CartesianProductLop(apply(left), right);
            } else {
                throw new UnsupportedOperationException(); // TODO: would need to add aliases, PITA
            }
        }

        @Override
        public LogicalOperator visit(FilterLop filter) {
            return new FilterLop(apply(filter.input()), new OuterQueryVariableReplacer().apply(filter.filter()));
        }

        @Override
        public LogicalOperator visit(InnerJoinLop innerJoin) {
            final LogicalOperator left = innerJoin.left();
            final LogicalOperator right = innerJoin.right();

            if (intersection(freeVariables(left), attributesD).isEmpty()) {
                return new InnerJoinLop(left, apply(right), innerJoin.joinSpecification());
            } else if (intersection(freeVariables(right), attributesD).isEmpty()) {
                return new InnerJoinLop(apply(left), right, innerJoin.joinSpecification());
            } else {
                throw new UnsupportedOperationException(); // TODO: would need to add aliases, PITA
            }
        }

        @Override
        public LogicalOperator visit(MapLop map) {
            final Set<ColumnReference> expressionOutputs = new HashSet<>(map.expressionsOutputNames());

            final List<ValueExpression> expandedFilters = expandPredicate();
            final List<ValueExpression> filtersAbove = new ArrayList<>();
            final List<ValueExpression> filtersBelow = new ArrayList<>();
            for (ValueExpression filter : expandedFilters) {
                if (variables(filter).anyMatch(expressionOutputs::contains)) {
                    filtersAbove.add(filter);
                } else {
                    filtersBelow.add(filter);
                }
            }

            final ValueExpression collapsedFilterBelow = collapseFilters(filtersBelow).orElse(null);
            final LogicalOperator transformedInput = new DependentJoinPushDownRules(d, collapsedFilterBelow)
                    .apply(map.input());

            final MapLop transformedNode = new MapLop(transformedInput,
                    map.expressions(),
                    map.expressionsOutputNames());

            return createFilterAbove(transformedNode, filtersAbove);
        }

        @Override
        public LogicalOperator visit(ProjectionLop projection) {
            final List<ColumnReference> newProjection = new ArrayList<>(projection.outputColumns());
            for (ColumnReference column : attributesD) {
                if (!newProjection.contains(column)) {
                    newProjection.add(column);
                }
            }

            return new ProjectionLop(apply(projection.input()), newProjection);
        }

        @Override
        public LogicalOperator visit(ScalarSubqueryLop scalarSubquery) {
            return scalarSubquery.transformInputs(this); // Fait à la va-vite avec ma bite
        }

        @Override
        public LogicalOperator apply(LogicalOperator right) {
            if (notDependent(right)) {
                return createJoinAndStopVisit(right);
            }

            return super.apply(right);
        }

        @Override
        public LogicalOperator defaultVisit(LogicalOperator node) {
            throw new UnsupportedOperationException(
                    "Unable to push a dependent join down a " + node.getClass().getSimpleName() + " node");
        }

        private boolean notDependent(LogicalOperator right) {
            return intersection(freeVariables(right), attributesD).isEmpty();
        }

        private LogicalOperator createJoinAndStopVisit(LogicalOperator right) {
            if (predicate != null) {
                return new InnerJoinLop(d, right, predicate);
            } else {
                return new CartesianProductLop(d, right);
            }
        }

        private Stream<ColumnReference> variables(ValueExpression valueExpression) {
            return variableCollector.apply(valueExpression).stream();
        }
    }
}
