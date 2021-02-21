package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.data.value.NullValue;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LeftJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SemiJoinLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.SubqueryExecutionLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.engine.optimizer.optimization.ConstantEvaluator.isConstant;
import static fr.awildelephant.rdbms.evaluator.input.NoValues.noValues;
import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterCollapser.collapseFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.OrExpressionFactorizer.factorizeOrExpression;
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

        final LogicalOperator transformedInput = new FilterPushDown(filtersOnInput).apply(aggregationNode.input());

        return createFilterAbove(filtersOnAggregates,
                new AggregationLop(transformedInput, aggregationNode.breakdowns(), aggregationNode.aggregates()));
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
    public LogicalOperator visit(DistinctLop distinctNode) {
        return new DistinctLop(apply(distinctNode.input()));
    }

    @Override
    public LogicalOperator visit(FilterLop filter) {
        final List<ValueExpression> allFilters = expandFilters(factorizeOrExpression(filter.filter()));
        allFilters.addAll(filters);

        return new FilterPushDown(allFilters).apply(filter.input());
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoin) {
        final Schema leftInputSchema = innerJoin.left().schema();
        final Schema rightInputSchema = innerJoin.right().schema();

        final List<ValueExpression> filtersOnLeftInput = new ArrayList<>();
        final List<ValueExpression> filtersOnRightInput = new ArrayList<>();
        final List<ValueExpression> joinConditionFilters = new ArrayList<>();
        final List<ValueExpression> filtersAbove = new ArrayList<>();

        final List<ValueExpression> allFilters = new ArrayList<>(filters);
        allFilters.addAll(expandFilters(innerJoin.joinSpecification()));

        for (ValueExpression expression : allFilters) {
            final List<ColumnReference> requiredVariables = expression.variables().collect(toList());

            final boolean requiresLeftInput = requiredVariables.stream().anyMatch(leftInputSchema::contains);
            final boolean requiresRightInput = requiredVariables.stream().anyMatch(rightInputSchema::contains);

            if (!requiresLeftInput) {
                filtersOnRightInput.add(expression);
            }

            if (!requiresRightInput) {
                filtersOnLeftInput.add(expression);
            }

            if (requiresLeftInput && requiresRightInput) {
                if (expression instanceof EqualExpression) {
                    joinConditionFilters.add(expression);
                } else {
                    filtersAbove.add(expression);
                }
            }
        }

        final LogicalOperator transformedLeftInput = new FilterPushDown(filtersOnLeftInput).apply(innerJoin.left());
        final LogicalOperator transformedRightInput = new FilterPushDown(filtersOnRightInput).apply(innerJoin.right());

        // Out of desperation, we pick non-equal predicates to use in the join condition
        if (joinConditionFilters.isEmpty() && !filtersAbove.isEmpty()) {
            joinConditionFilters.addAll(filtersAbove);
            filtersAbove.clear();
        }

        final Optional<ValueExpression> joinCondition = collapseFilters(joinConditionFilters);
        final LogicalOperator transformedJoin;
        if (joinCondition.isPresent()) {
            transformedJoin = new InnerJoinLop(transformedLeftInput,
                                               transformedRightInput,
                                               joinCondition.get(),
                                               innerJoin.schema());
        } else {
            transformedJoin = new CartesianProductLop(transformedLeftInput,
                                                      transformedRightInput,
                                                      innerJoin.schema());
        }

        return createFilterAbove(filtersAbove, transformedJoin);
    }

    // TODO: reminder to go back and improve this code
    @Override
    public LogicalOperator visit(LeftJoinLop leftJoin) {
        final Schema leftInputSchema = leftJoin.left().schema();
        final Schema rightInputSchema = leftJoin.right().schema();

        final List<ValueExpression> filtersOnLeftInput = new ArrayList<>();
        final List<ValueExpression> filtersOnRightInput = new ArrayList<>();
        final List<ValueExpression> filtersAbove = new ArrayList<>();

        boolean transformToInnerJoin = false;

        for (ValueExpression expression : filters) {
            final List<ColumnReference> requiredVariables = expression.variables().collect(toList());

            final boolean requiresLeftInput = requiredVariables.stream().anyMatch(leftInputSchema::contains);
            final boolean requiresRightInput = requiredVariables.stream().anyMatch(rightInputSchema::contains);

            if (!requiresLeftInput) {
                if (shouldTransformLeftJoinToInnerJoin(expression, rightInputSchema)) {
                    filtersOnRightInput.add(expression);
                    transformToInnerJoin = true;
                } else {
                    filtersAbove.add(expression);
                }
            }

            if (!requiresRightInput) {
                filtersOnLeftInput.add(expression);
            }

            if (requiresLeftInput && requiresRightInput) {
                filtersAbove.add(expression);
            }
        }

        final List<ValueExpression> joinFilters = new ArrayList<>();

        for (ValueExpression expression : expandFilters(leftJoin.joinSpecification())) {
            final List<ColumnReference> requiredVariables = expression.variables().collect(toList());

            final boolean requiresLeftInput = requiredVariables.stream().anyMatch(leftInputSchema::contains);
            final boolean requiresRightInput = requiredVariables.stream().anyMatch(rightInputSchema::contains);

            if (!requiresLeftInput) {
                filtersOnRightInput.add(expression);
            }

            if (!requiresRightInput) {
                filtersOnLeftInput.add(expression);
            }

            if (requiresLeftInput && requiresRightInput) {
                joinFilters.add(expression);
            }
        }

        final LogicalOperator transformedLeftInput = new FilterPushDown(filtersOnLeftInput).apply(leftJoin.left());
        final LogicalOperator transformedRightInput = new FilterPushDown(filtersOnRightInput).apply(leftJoin.right());

        final Optional<ValueExpression> joinCondition = collapseFilters(joinFilters);

        if (joinCondition.isPresent()) {
            final LogicalOperator transformedJoin;
            if (transformToInnerJoin) {
                transformedJoin = new InnerJoinLop(transformedLeftInput,
                                                   transformedRightInput,
                                                   joinCondition.get(),
                                                   innerJoinOutputSchema(leftInputSchema, rightInputSchema));
            } else {
                transformedJoin = new LeftJoinLop(transformedLeftInput,
                                                  transformedRightInput,
                                                  joinCondition.get(),
                                                  leftJoin.schema());
            }

            return createFilterAbove(filtersAbove, transformedJoin);
        } else {
            final Schema outputSchema = innerJoinOutputSchema(leftInputSchema, rightInputSchema);

            return collapseFilters(filtersAbove)
                    .<LogicalOperator>map(filter -> new InnerJoinLop(transformedLeftInput,
                                                                     transformedRightInput,
                                                                     filter,
                                                                     outputSchema))
                    .orElseGet(() -> new CartesianProductLop(transformedLeftInput,
                                                             transformedRightInput,
                                                             outputSchema));
        }
    }

    private boolean shouldTransformLeftJoinToInnerJoin(ValueExpression expression, Schema rightInputSchema) {
        final Map<ColumnReference, NullValue> nullValues = rightInputSchema.columnNames().stream()
                .collect(Collectors.toMap(Function.identity(), unused -> nullValue()));

        final ValueExpression transformedExpression = new VariableTransformer(nullValues).apply(expression);
        final ValueExpression simplifiedExpression = new ExpressionSimplifier().apply(transformedExpression);

        if (isConstant(simplifiedExpression)) {
            final Formula formula = createFormula(simplifiedExpression, Schema.emptySchema());
            final DomainValue constantValue = formula.evaluate(noValues());

            return constantValue.isNull() || !constantValue.getBool();
        }

        return false;
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
                        mapNode.expressions(),
                        mapNode.expressionsOutputNames()));
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
    public LogicalOperator visit(SemiJoinLop semiJoin) {
        final String outputColumnName = semiJoin.ouputColumnName();

        final List<ValueExpression> regularFilters = new ArrayList<>();
        final List<ValueExpression> filtersReferencingSemiJoin = new ArrayList<>();

        for (ValueExpression expression : filters) {
            final List<ColumnReference> requiredVariables = expression.variables().collect(toList());

            final boolean referencesSemiJoin = requiredVariables.stream().anyMatch(semiJoinReference(outputColumnName));

            if (referencesSemiJoin) {
                filtersReferencingSemiJoin.add(expression);
            } else {
                regularFilters.add(expression);
            }
        }

        final LogicalOperator transformedLeftInput = new FilterPushDown(regularFilters).apply(semiJoin.left());

        final SemiJoinLop transformedJoin = new SemiJoinLop(
                transformedLeftInput,
                new FilterPushDown().apply(semiJoin.right()),
                semiJoin.predicate(),
                semiJoin.ouputColumnName());

        return createFilterAbove(filtersReferencingSemiJoin, transformedJoin);
    }

    private Predicate<ColumnReference> semiJoinReference(String semiJoinOutputColumnName) {
        return columnReference -> {
            if (columnReference instanceof QualifiedColumnReference) {
                return false;
            }

            return semiJoinOutputColumnName.equals(columnReference.name());
        };
    }

    @Override
    public LogicalOperator visit(SortLop sortLop) {
        return new SortLop(apply(sortLop.input()), sortLop.sortSpecificationList());
    }

    @Override
    public LogicalOperator visit(SubqueryExecutionLop subqueryExecutionLop) {
        final Schema subquerySchema = subqueryExecutionLop.subquery().schema();

        final Collection<ValueExpression> filtersOnInput = new ArrayList<>();
        final Collection<ValueExpression> filtersOnBoth = new ArrayList<>();

        for (ValueExpression filter : filters) {
            final List<ColumnReference> requiredVariables = filter.variables().collect(toList());

            final boolean requiresSubquery = requiredVariables.stream().anyMatch(subquerySchema::contains);

            if (!requiresSubquery) {
                filtersOnInput.add(filter);
            } else {
                filtersOnBoth.add(filter);
            }
        }

        return createFilterAbove(filtersOnBoth, new SubqueryExecutionLop(
                new FilterPushDown(filtersOnInput).apply(subqueryExecutionLop.input()),
                new FilterPushDown().apply(subqueryExecutionLop.subquery())
        ));
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
