package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.engine.optimizer.optimization.ExpressionAliaser;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DependentSemiJoinLop;
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
import fr.awildelephant.rdbms.plan.DependentJoinLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.alias.ExactMatchAlias;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.sort.SortSpecification;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static fr.awildelephant.rdbms.plan.sort.SortSpecification.ascending;
import static fr.awildelephant.rdbms.plan.sort.SortSpecification.descending;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class AliasSimplification implements LopVisitor<LogicalOperator> {

    private final Map<ColumnReference, ColumnReference> aliasing;

    public AliasSimplification() {
        this(new HashMap<>());
    }

    public AliasSimplification(Map<ColumnReference, ColumnReference> aliasing) {
        this.aliasing = aliasing;
    }

    /**
     * If aggregations columns are aliased, set the alias as output name. Push the rest of the aliases down.
     */
    @Override
    public LogicalOperator visit(AggregationLop node) {
        final Set<ColumnReference> aggregatesOutputColumns = node.aggregates().stream()
                .map(Aggregate::outputColumn)
                .collect(toSet());

        final Map<ColumnReference, ColumnReference> inputAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> aggregatesAliasing = new HashMap<>();

        aliasing.forEach((original, alias) -> {
            if (aggregatesOutputColumns.contains(original)) {
                aggregatesAliasing.put(original, alias);
            } else {
                inputAliasing.put(original, alias);
            }
        });

        final ExactMatchAlias inputAliaser = new ExactMatchAlias(inputAliasing);

        final List<ColumnReference> aliasedBreakdowns = node.breakdowns().stream()
                .map(inputAliaser::alias)
                .collect(toList());

        final ExactMatchAlias aggregateOutputAliaser = new ExactMatchAlias(aggregatesAliasing);
        final AggregateAliaser aggregateAliaser = new AggregateAliaser(inputAliaser, aggregateOutputAliaser);

        final List<Aggregate> aliasedAggregates = node.aggregates().stream()
                .map(aggregateAliaser::alias)
                .collect(toList());

        final LogicalOperator transformedInput = new AliasSimplification(inputAliasing).apply(node.input());

        return new AggregationLop(transformedInput, aliasedBreakdowns, aliasedAggregates);
    }

    /**
     * Update aliasing and continue pushing down.
     */
    @Override
    public LogicalOperator visit(AliasLop node) {
        final Alias nodeAliasing = node.alias();

        final Schema inputSchema = node.input().schema();

        final Map<ColumnReference, ColumnReference> newAliasing = new HashMap<>();
        for (ColumnReference columnName : inputSchema.columnNames()) {
            final ColumnReference alias = nodeAliasing.alias(columnName);
            final ColumnReference existingAlias = aliasing.get(alias);

            if (existingAlias != null) {
                newAliasing.put(columnName, existingAlias);
            } else {
                newAliasing.put(columnName, alias);
            }
        }

        return new AliasSimplification(newAliasing).apply(node.input());
    }

    /**
     * Leaf node, put the remaining aliases over it.
     */
    @Override
    public LogicalOperator visit(BaseTableLop node) {
        if (aliasing.isEmpty()) {
            return node;
        }

        return new AliasLop(node, new ExactMatchAlias(aliasing));
    }

    @Override
    public LogicalOperator visit(CartesianProductLop node) {
        final LogicalOperator leftInput = node.leftInput();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = node.rightInput();
        final Schema rightInputSchema = rightInput.schema();

        final Map<ColumnReference, ColumnReference> leftAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> rightAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (leftInputSchema.contains(original)) {
                leftAliasing.put(original, alias);
            }
            if (rightInputSchema.contains(original)) {
                rightAliasing.put(original, alias);
            }
        });

        return new CartesianProductLop(new AliasSimplification(leftAliasing).apply(leftInput),
                                       new AliasSimplification(rightAliasing).apply(rightInput));
    }

    @Override
    public LogicalOperator visit(DistinctLop node) {
        return node.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(FilterLop node) {
        final ExpressionAliaser aliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));

        return new FilterLop(apply(node.input()), aliaser.apply(node.filter()));
    }

    @Override
    public LogicalOperator visit(InnerJoinLop node) {
        final LogicalOperator leftInput = node.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = node.right();
        final Schema rightInputSchema = rightInput.schema();

        final Map<ColumnReference, ColumnReference> leftAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> rightAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (leftInputSchema.contains(original)) {
                leftAliasing.put(original, alias);
            }
            if (rightInputSchema.contains(original)) {
                rightAliasing.put(original, alias);
            }
        });

        final ExpressionAliaser expressionAliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));

        final ValueExpression aliasedJoinSpecification = expressionAliaser.apply(node.joinSpecification());

        return new InnerJoinLop(new AliasSimplification(leftAliasing).apply(leftInput),
                                new AliasSimplification(rightAliasing).apply(rightInput),
                                aliasedJoinSpecification);
    }

    @Override
    public LogicalOperator visit(LeftJoinLop node) {
        final LogicalOperator leftInput = node.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = node.right();
        final Schema rightInputSchema = rightInput.schema();

        final Map<ColumnReference, ColumnReference> leftAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> rightAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (leftInputSchema.contains(original)) {
                leftAliasing.put(original, alias);
            }
            if (rightInputSchema.contains(original)) {
                rightAliasing.put(original, alias);
            }
        });

        final ExpressionAliaser expressionAliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));

        final ValueExpression aliasedJoinSpecification = expressionAliaser.apply(node.joinSpecification());

        return new LeftJoinLop(new AliasSimplification(leftAliasing).apply(leftInput),
                                new AliasSimplification(rightAliasing).apply(rightInput),
                                aliasedJoinSpecification);
    }

    @Override
    public LogicalOperator visit(LimitLop node) {
        return node.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(MapLop node) {
        final HashSet<ColumnReference> unaliasedExpressionsOutputNames = new HashSet<>(node.expressionsOutputNames());
        final Map<ColumnReference, ColumnReference> inputAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> expressionsAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (unaliasedExpressionsOutputNames.contains(original)) {
                expressionsAliasing.put(original, alias);
            } else {
                inputAliasing.put(original, alias);
            }
        });

        final LogicalOperator transformedInput = new AliasSimplification(inputAliasing).apply(node.input());

        final ExpressionAliaser expressionAliaser = new ExpressionAliaser(new ExactMatchAlias(inputAliasing));

        final List<ValueExpression> aliasedExpressions = node.expressions().stream()
                .map(expressionAliaser)
                .collect(toList());

        final ExactMatchAlias alias = new ExactMatchAlias(expressionsAliasing);

        final List<ColumnReference> aliasedExpressionOutputNames = node.expressionsOutputNames().stream()
                .map(alias::alias)
                .collect(toList());

        return new MapLop(transformedInput, aliasedExpressions, aliasedExpressionOutputNames);
    }

    @Override
    public LogicalOperator visit(ProjectionLop node) {
        final ExactMatchAlias alias = new ExactMatchAlias(aliasing);

        final List<ColumnReference> aliasedOutputColumns = node.outputColumns().stream()
                .map(alias::alias)
                .collect(toList());

        return new ProjectionLop(apply(node.input()), aliasedOutputColumns);
    }

    @Override
    public LogicalOperator visit(ScalarSubqueryLop scalarSubquery) {
        return scalarSubquery.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(DependentSemiJoinLop semiJoin) {
        final LogicalOperator leftInput = semiJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = semiJoin.right();
        final Schema rightInputSchema = rightInput.schema();

        final Map<ColumnReference, ColumnReference> leftAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> rightAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (leftInputSchema.contains(original)) {
                leftAliasing.put(original, alias);
            }
            if (rightInputSchema.contains(original)) {
                rightAliasing.put(original, alias);
            }
        });

        final ExpressionAliaser expressionAliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));

        final ValueExpression aliasedJoinSpecification = expressionAliaser.apply(semiJoin.predicate());

        ColumnReference aliasedSemiJoinOutputColumn = aliasing.get(semiJoin.outputColumnName());
        if (aliasedSemiJoinOutputColumn == null) {
            aliasedSemiJoinOutputColumn = semiJoin.outputColumnName();
        }

        return new DependentSemiJoinLop(new AliasSimplification(leftAliasing).apply(leftInput),
                               new AliasSimplification(rightAliasing).apply(rightInput),
                               aliasedJoinSpecification,
                               aliasedSemiJoinOutputColumn);
    }

    @Override
    public LogicalOperator visit(SemiJoinLop semiJoin) {
        final LogicalOperator leftInput = semiJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = semiJoin.right();
        final Schema rightInputSchema = rightInput.schema();

        final Map<ColumnReference, ColumnReference> leftAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> rightAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (leftInputSchema.contains(original)) {
                leftAliasing.put(original, alias);
            }
            if (rightInputSchema.contains(original)) {
                rightAliasing.put(original, alias);
            }
        });

        final ExpressionAliaser expressionAliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));

        final ValueExpression aliasedJoinSpecification = expressionAliaser.apply(semiJoin.predicate());

        ColumnReference aliasedSemiJoinOutputColumn = aliasing.get(semiJoin.outputColumnName());
        if (aliasedSemiJoinOutputColumn == null) {
            aliasedSemiJoinOutputColumn = semiJoin.outputColumnName();
        }

        return new SemiJoinLop(new AliasSimplification(leftAliasing).apply(leftInput),
                               new AliasSimplification(rightAliasing).apply(rightInput),
                               aliasedJoinSpecification,
                               aliasedSemiJoinOutputColumn);
    }

    @Override
    public LogicalOperator visit(SortLop node) {
        final ExactMatchAlias aliaser = new ExactMatchAlias(aliasing);
        final List<SortSpecification> aliasedSortSpecificationList = node.sortSpecificationList().stream()
                .map(specification -> {
                    final ColumnReference aliasedColumn = aliaser.alias(specification.column());
                    if (specification.ascending()) {
                        return ascending(aliasedColumn);
                    } else {
                        return descending(aliasedColumn);
                    }
                })
                .collect(toList());

        return new SortLop(apply(node.input()), aliasedSortSpecificationList);
    }

    @Override
    public LogicalOperator visit(DependentJoinLop node) {
        final LogicalOperator leftInput = node.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = node.right();
        final Schema rightInputSchema = rightInput.schema();

        final Map<ColumnReference, ColumnReference> leftAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> rightAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (leftInputSchema.contains(original)) {
                leftAliasing.put(original, alias);
            }
            if (rightInputSchema.contains(original)) {
                rightAliasing.put(original, alias);
            }
        });

        final ExpressionAliaser aliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));
        final ValueExpression aliasedPredicate = aliaser.apply(node.predicate());

        return new DependentJoinLop(new AliasSimplification(leftAliasing).apply(leftInput),
                                    new AliasSimplification(rightAliasing).apply(rightInput),
                                    aliasedPredicate);
    }

    /**
     * Leaf node, put the remaining aliases over it.
     */
    @Override
    public LogicalOperator visit(TableConstructorLop node) {
        if (aliasing.isEmpty()) {
            return node;
        }

        return new AliasLop(node, new ExactMatchAlias(aliasing));
    }
}
