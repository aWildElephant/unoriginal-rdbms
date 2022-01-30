package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.data.value.NullValue;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.DependentJoinLop;
import fr.awildelephant.rdbms.plan.DependentSemiJoinLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LeftJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.SemiJoinLop;
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
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
public final class FilterPushDown extends DefaultLopVisitor<LogicalOperator> {

    private final Collection<ValueExpression> filters;

    public FilterPushDown() {
        this(Collections.emptyList());
    }

    private FilterPushDown(Collection<ValueExpression> filters) {
        this.filters = filters;
    }

    @Override
    public LogicalOperator visit(AggregationLop aggregation) {
        final List<ColumnReference> aggregatesColumns = aggregation.aggregates()
                .stream()
                .map(Aggregate::outputColumn)
                .toList();

        final List<ValueExpression> filtersOnAggregates = new ArrayList<>();
        final List<ValueExpression> filtersOnInput = new ArrayList<>();

        for (ValueExpression filter : filters) {
            if (filter.variables().anyMatch(aggregatesColumns::contains)) {
                filtersOnAggregates.add(filter);
            } else {
                filtersOnInput.add(filter);
            }
        }

        final LogicalOperator transformedInput = new FilterPushDown(filtersOnInput).apply(aggregation.input());

        return createFilterAbove(filtersOnAggregates,
                new AggregationLop(transformedInput, aggregation.breakdowns(),
                        aggregation.aggregates()));
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
    public LogicalOperator visit(DependentJoinLop dependentJoin) {
        final LogicalOperator leftInput = dependentJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = dependentJoin.right();
        final Schema rightInputSchema = rightInput.schema();

        final Collection<ValueExpression> filtersOnLeft = new ArrayList<>();
        final Collection<ValueExpression> filtersOnRight = new ArrayList<>();
        final Collection<ValueExpression> filtersOnBoth = new ArrayList<>();

        for (ValueExpression filter : filters) {
            final List<ColumnReference> requiredVariables = filter.variables().toList();

            final boolean requiresRightInput = requiredVariables.stream().anyMatch(rightInputSchema::contains);
            final boolean requiresLeftInput = requiredVariables.stream().anyMatch(leftInputSchema::contains);

            if (!requiresRightInput) {
                filtersOnLeft.add(filter);
            }

            if (!requiresLeftInput) {
                filtersOnRight.add(filter);
            }

            if (requiresLeftInput && requiresRightInput) {
                filtersOnBoth.add(filter);
            }
        }

        final Optional<ValueExpression> predicate = collapseFilters(filtersOnBoth);

        final LogicalOperator transformedLeftInput = new FilterPushDown(filtersOnLeft).apply(leftInput);
        final LogicalOperator transformedRightInput = new FilterPushDown(filtersOnRight).apply(rightInput);

        return predicate
                .map(expression -> new DependentJoinLop(transformedLeftInput, transformedRightInput, expression))
                .orElseGet(() -> new DependentJoinLop(transformedLeftInput, transformedRightInput));
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
            final List<ColumnReference> requiredVariables = expression.variables().toList();

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
            final List<ColumnReference> requiredVariables = expression.variables().toList();

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
            final List<ColumnReference> requiredVariables = expression.variables().toList();

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
            final Formula formula = createFormula(simplifiedExpression, Schema.empty());
            final DomainValue constantValue = formula.evaluate(noValues());

            return constantValue.isNull() || !constantValue.getBool();
        }

        return false;
    }

    @Override
    public LogicalOperator visit(LimitLop limit) {
        return new LimitLop(apply(limit.input()), limit.limit());
    }

    @Override
    public LogicalOperator visit(MapLop map) {
        final Schema schema = map.schema();

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
                new MapLop(new FilterPushDown(filtersOnInput).apply(map.input()),
                        map.expressions(),
                        map.expressionsOutputNames()));
    }

    @Override
    public LogicalOperator visit(ProjectionLop projection) {
        return new ProjectionLop(apply(projection.input()), projection.outputColumns());
    }

    @Override
    public LogicalOperator visit(DependentSemiJoinLop dependentSemiJoin) {
        final ColumnReference outputColumnName = dependentSemiJoin.outputColumnName();

        final List<ValueExpression> regularFilters = new ArrayList<>();
        final List<ValueExpression> filtersReferencingSemiJoin = new ArrayList<>();

        for (ValueExpression expression : filters) {
            final List<ColumnReference> requiredVariables = expression.variables().toList();

            final boolean referencesSemiJoin = requiredVariables.stream().anyMatch(outputColumnName::equals);

            if (referencesSemiJoin) {
                filtersReferencingSemiJoin.add(expression);
            } else {
                regularFilters.add(expression);
            }
        }

        final LogicalOperator transformedLeftInput = new FilterPushDown(regularFilters).apply(dependentSemiJoin.left());

        final DependentSemiJoinLop transformedJoin = new DependentSemiJoinLop(
                transformedLeftInput,
                new FilterPushDown().apply(dependentSemiJoin.right()),
                dependentSemiJoin.predicate(),
                dependentSemiJoin.outputColumnName());

        return createFilterAbove(filtersReferencingSemiJoin, transformedJoin);
    }

    @Override
    public LogicalOperator visit(SemiJoinLop semiJoin) {
        final ColumnReference outputColumnName = semiJoin.outputColumnName();

        final List<ValueExpression> regularFilters = new ArrayList<>();
        final List<ValueExpression> filtersReferencingSemiJoin = new ArrayList<>();

        for (ValueExpression expression : filters) {
            final List<ColumnReference> requiredVariables = expression.variables().toList();

            final boolean referencesSemiJoin = requiredVariables.stream().anyMatch(outputColumnName::equals);

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
                semiJoin.outputColumnName());

        return createFilterAbove(filtersReferencingSemiJoin, transformedJoin);
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

        final Schema leftSchema = cartesianProduct.left().schema();
        final Schema rightSchema = cartesianProduct.right().schema();

        for (ValueExpression filter : filters) {
            final List<ColumnReference> requiredVariables = filter.variables().toList();

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

        final LogicalOperator leftInput = new FilterPushDown(leftFilters).apply(cartesianProduct.left());
        final LogicalOperator rightInput = new FilterPushDown(rightFilters).apply(cartesianProduct.right());

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

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
