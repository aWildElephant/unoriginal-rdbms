package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.engine.optimizer.optimization.ExpressionAliaser;
import fr.awildelephant.rdbms.operator.logical.AggregationLop;
import fr.awildelephant.rdbms.operator.logical.AliasLop;
import fr.awildelephant.rdbms.operator.logical.BaseTableLop;
import fr.awildelephant.rdbms.operator.logical.CartesianProductLop;
import fr.awildelephant.rdbms.operator.logical.DependentJoinLop;
import fr.awildelephant.rdbms.operator.logical.DependentSemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.DistinctLop;
import fr.awildelephant.rdbms.operator.logical.FilterLop;
import fr.awildelephant.rdbms.operator.logical.InnerJoinLop;
import fr.awildelephant.rdbms.operator.logical.LeftJoinLop;
import fr.awildelephant.rdbms.operator.logical.LimitLop;
import fr.awildelephant.rdbms.operator.logical.LogicalOperator;
import fr.awildelephant.rdbms.operator.logical.LopVisitor;
import fr.awildelephant.rdbms.operator.logical.MapLop;
import fr.awildelephant.rdbms.operator.logical.ProjectionLop;
import fr.awildelephant.rdbms.operator.logical.ReadCSVLop;
import fr.awildelephant.rdbms.operator.logical.ScalarSubqueryLop;
import fr.awildelephant.rdbms.operator.logical.SemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.SortLop;
import fr.awildelephant.rdbms.operator.logical.TableConstructorLop;
import fr.awildelephant.rdbms.operator.logical.aggregation.Aggregate;
import fr.awildelephant.rdbms.operator.logical.alias.Alias;
import fr.awildelephant.rdbms.operator.logical.alias.ExactMatchAlias;
import fr.awildelephant.rdbms.operator.logical.sort.SortSpecification;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public LogicalOperator visit(AggregationLop aggregation) {
        final Set<ColumnReference> aggregatesOutputColumns = aggregation.aggregates().stream()
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

        final List<ColumnReference> aliasedBreakdowns = aggregation.breakdowns().stream()
                .map(inputAliaser::alias)
                .collect(toList());

        final ExactMatchAlias aggregateOutputAliaser = new ExactMatchAlias(aggregatesAliasing);
        final AggregateAliaser aggregateAliaser = new AggregateAliaser(inputAliaser, aggregateOutputAliaser);

        final List<Aggregate> aliasedAggregates = aggregation.aggregates().stream()
                .map(aggregateAliaser::alias)
                .collect(toList());

        final LogicalOperator transformedInput = new AliasSimplification(inputAliasing).apply(aggregation.input());

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
    public LogicalOperator visit(CartesianProductLop cartesianProduct) {
        final LogicalOperator leftInput = cartesianProduct.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = cartesianProduct.right();
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
    public LogicalOperator visit(DistinctLop distinct) {
        return distinct.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(FilterLop node) {
        final ExpressionAliaser aliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));

        return new FilterLop(apply(node.input()), aliaser.apply(node.filter()));
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoin) {
        final LogicalOperator leftInput = innerJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = innerJoin.right();
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

        final ValueExpression aliasedJoinSpecification = expressionAliaser.apply(innerJoin.joinSpecification());

        return new InnerJoinLop(new AliasSimplification(leftAliasing).apply(leftInput),
                                new AliasSimplification(rightAliasing).apply(rightInput),
                                aliasedJoinSpecification);
    }

    @Override
    public LogicalOperator visit(LeftJoinLop leftJoin) {
        final LogicalOperator leftInput = leftJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = leftJoin.right();
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

        final ValueExpression aliasedJoinSpecification = expressionAliaser.apply(leftJoin.joinSpecification());

        return new LeftJoinLop(new AliasSimplification(leftAliasing).apply(leftInput),
                               new AliasSimplification(rightAliasing).apply(rightInput),
                               aliasedJoinSpecification);
    }

    @Override
    public LogicalOperator visit(LimitLop limit) {
        return limit.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(MapLop map) {
        final HashSet<ColumnReference> unaliasedExpressionsOutputNames = new HashSet<>(map.expressionsOutputNames());
        final Map<ColumnReference, ColumnReference> inputAliasing = new HashMap<>();
        final Map<ColumnReference, ColumnReference> expressionsAliasing = new HashMap<>();
        aliasing.forEach((original, alias) -> {
            if (unaliasedExpressionsOutputNames.contains(original)) {
                expressionsAliasing.put(original, alias);
            } else {
                inputAliasing.put(original, alias);
            }
        });

        final LogicalOperator transformedInput = new AliasSimplification(inputAliasing).apply(map.input());

        final ExpressionAliaser expressionAliaser = new ExpressionAliaser(new ExactMatchAlias(inputAliasing));

        final List<ValueExpression> aliasedExpressions = map.expressions().stream()
                .map(expressionAliaser)
                .collect(toList());

        final ExactMatchAlias alias = new ExactMatchAlias(expressionsAliasing);

        final List<ColumnReference> aliasedExpressionOutputNames = map.expressionsOutputNames().stream()
                .map(alias::alias)
                .collect(toList());

        return new MapLop(transformedInput, aliasedExpressions, aliasedExpressionOutputNames);
    }

    @Override
    public LogicalOperator visit(ProjectionLop projection) {
        final ExactMatchAlias alias = new ExactMatchAlias(aliasing);

        final List<ColumnReference> aliasedOutputColumns = projection.outputColumns().stream()
                .map(alias::alias)
                .collect(toList());

        return new ProjectionLop(apply(projection.input()), aliasedOutputColumns);
    }

    @Override
    public LogicalOperator visit(ScalarSubqueryLop scalarSubquery) {
        return scalarSubquery.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(DependentSemiJoinLop dependentSemiJoin) {
        final LogicalOperator leftInput = dependentSemiJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = dependentSemiJoin.right();
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

        final ValueExpression aliasedPredicate;
        if (dependentSemiJoin.predicate() != null) {
            final ExpressionAliaser expressionAliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));
            aliasedPredicate = expressionAliaser.apply(dependentSemiJoin.predicate());
        } else {
            aliasedPredicate = null;
        }

        ColumnReference aliasedSemiJoinOutputColumn = aliasing.get(dependentSemiJoin.outputColumnName());
        if (aliasedSemiJoinOutputColumn == null) {
            aliasedSemiJoinOutputColumn = dependentSemiJoin.outputColumnName();
        }

        final LogicalOperator transformedLeftInput = new AliasSimplification(leftAliasing).apply(leftInput);
        final LogicalOperator transformedRightInput = new AliasSimplification(rightAliasing).apply(
                new FreeVariableAliasing(new ExactMatchAlias(leftAliasing)).apply(rightInput));

        return new DependentSemiJoinLop(transformedLeftInput,
                                        transformedRightInput,
                                        aliasedPredicate,
                                        aliasedSemiJoinOutputColumn);
    }

    @Override
    public LogicalOperator visit(ReadCSVLop readCSV) {
        if (aliasing.isEmpty()) {
            return readCSV;
        }

        final Schema aliasedSchema = readCSV.schema().alias(reference -> aliasing.getOrDefault(reference, reference));

        return new ReadCSVLop(readCSV.filePath(), aliasedSchema);
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
    public LogicalOperator visit(SortLop sort) {
        final ExactMatchAlias aliaser = new ExactMatchAlias(aliasing);
        final List<SortSpecification> aliasedSortSpecificationList = sort.sortSpecificationList().stream()
                .map(specification -> {
                    final ColumnReference aliasedColumn = aliaser.alias(specification.column());
                    return new SortSpecification(aliasedColumn, specification.ascending(), specification.nullsLast());
                })
                .collect(toList());

        return new SortLop(apply(sort.input()), aliasedSortSpecificationList);
    }

    @Override
    public LogicalOperator visit(DependentJoinLop dependentJoin) {
        final LogicalOperator leftInput = dependentJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = dependentJoin.right();
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

        final ValueExpression aliasedPredicate;
        if (dependentJoin.predicate() != null) {
            final ExpressionAliaser aliaser = new ExpressionAliaser(new ExactMatchAlias(aliasing));
            aliasedPredicate = aliaser.apply(dependentJoin.predicate());
        } else {
            aliasedPredicate = null;
        }

        final LogicalOperator transformedLeftInput = new AliasSimplification(leftAliasing).apply(leftInput);
        final LogicalOperator transformedRightInput = new AliasSimplification(rightAliasing).apply(
                new FreeVariableAliasing(new ExactMatchAlias(leftAliasing)).apply(rightInput));

        return new DependentJoinLop(transformedLeftInput, transformedRightInput, aliasedPredicate);
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
