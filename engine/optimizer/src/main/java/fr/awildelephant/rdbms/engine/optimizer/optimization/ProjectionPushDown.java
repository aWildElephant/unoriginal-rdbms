package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.*;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.*;

import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.leftJoinOutputSchema;
import static java.util.stream.Collectors.toList;

public final class ProjectionPushDown extends DefaultLopVisitor<LogicalOperator> {

    private final List<ColumnReference> projection;

    public ProjectionPushDown(List<ColumnReference> projection) {
        this.projection = projection;
    }

    public static LogicalOperator pushDownProjections(LogicalOperator operator) {
        return new ProjectionPushDown(operator.schema().columnNames()).apply(operator);
    }

    @Override
    public LogicalOperator visit(AggregationLop aggregationNode) {
        final List<ColumnReference> extendedProjection
                = extendProjectionWithColumnsRequiredForAggregation(aggregationNode);

        final LogicalOperator transformedInput = new ProjectionPushDown(
                restrictProjectionToAvailableColumns(aggregationNode.input(), extendedProjection)
        ).apply(aggregationNode.input());

        final LogicalOperator transformedNode = new AggregationLop(transformedInput, aggregationNode.breakdowns(),
                                                                   aggregationNode.aggregates());

        return createProjectionIfNecessary(transformedNode);
    }

    private List<ColumnReference> extendProjectionWithColumnsRequiredForAggregation(AggregationLop aggregationNode) {
        final Set<ColumnReference> columnsRequiredForAggregates = new HashSet<>(aggregationNode.breakdowns());
        for (Aggregate aggregate : aggregationNode.aggregates()) {
            aggregate.inputColumn().ifPresent(columnsRequiredForAggregates::add);
        }

        return merge(projection, aggregationNode.input().schema(), columnsRequiredForAggregates);
    }

    @Override
    public LogicalOperator visit(AliasLop aliasNode) {
        final Alias alias = aliasNode.alias();

        final List<ColumnReference> unaliasedProjection = projection.stream().map(alias::unalias)
                .collect(toList());

        final LogicalOperator transformedInput = new ProjectionPushDown(unaliasedProjection).apply(aliasNode.input());
        return new AliasLop(transformedInput, alias);
    }

    private List<ColumnReference> merge(List<ColumnReference> projectionOverNode,
                                        Schema inputSchema,
                                        Collection<ColumnReference> columnsRequiredByNode) {
        final List<ColumnReference> mergeResult = new ArrayList<>(projectionOverNode);
        for (ColumnReference column : columnsRequiredByNode) {
            final ColumnReference normalizedColumn = inputSchema.normalize(column);
            if (!mergeResult.contains(normalizedColumn)) {
                mergeResult.add(normalizedColumn);
            }
        }
        return mergeResult;
    }

    @Override
    public LogicalOperator visit(CartesianProductLop cartesianProductNode) {
        final LogicalOperator leftInput = cartesianProductNode.leftInput();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = cartesianProductNode.rightInput();
        final Schema rightInputSchema = rightInput.schema();

        final List<ColumnReference> leftInputProjection = new ArrayList<>();
        final List<ColumnReference> rightInputProjection = new ArrayList<>();
        for (ColumnReference column : projection) {
            if (leftInputSchema.contains(column)) {
                leftInputProjection.add(column);
            } else if (rightInputSchema.contains(column)) {
                rightInputProjection.add(column);
            }
        }

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(rightInputProjection).apply(rightInput);

        return new CartesianProductLop(transformedLeftInput,
                transformedRightInput,
                innerJoinOutputSchema(transformedLeftInput.schema(), transformedRightInput.schema()));
    }

    @Override
    public LogicalOperator visit(DistinctLop distinctNode) {
        return distinctNode.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(FilterLop filterNode) {
        final List<ColumnReference> columnsRequiredForFilter = filterNode.filter().variables().collect(toList());

        final List<ColumnReference> newProjection = merge(projection,
                filterNode.input().schema(),
                columnsRequiredForFilter);

        final LogicalOperator transformedFilter = filterNode.transformInputs(new ProjectionPushDown(newProjection));

        if (projection.size() != newProjection.size()) {
            return new ProjectionLop(transformedFilter, projection);
        } else {
            return transformedFilter;
        }
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoinLop) {
        final List<ColumnReference> newProjection = merge(projection,
                innerJoinLop.schema(),
                innerJoinLop.joinSpecification()
                        .variables()
                        .collect(toList()));

        final LogicalOperator leftInput = innerJoinLop.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = innerJoinLop.right();
        final Schema rightInputSchema = rightInput.schema();

        final List<ColumnReference> leftInputProjection = new ArrayList<>();
        final List<ColumnReference> rightInputProjection = new ArrayList<>();
        for (ColumnReference column : newProjection) {
            if (leftInputSchema.contains(column)) {
                leftInputProjection.add(column);
            } else if (rightInputSchema.contains(column)) {
                rightInputProjection.add(column);
            }
        }

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(rightInputProjection).apply(rightInput);
        final InnerJoinLop transformedJoin = new InnerJoinLop(transformedLeftInput,
                transformedRightInput,
                innerJoinLop.joinSpecification(),
                innerJoinOutputSchema(transformedLeftInput.schema(), transformedRightInput.schema()));

        if (newProjection.size() != projection.size()) {
            return new ProjectionLop(transformedJoin, projection);
        } else {
            return transformedJoin;
        }
    }

    @Override
    public LogicalOperator visit(LeftJoinLop leftJoinLop) {
        final List<ColumnReference> newProjection = merge(projection,
                leftJoinLop.schema(),
                leftJoinLop.joinSpecification()
                        .variables()
                        .collect(toList()));

        final LogicalOperator leftInput = leftJoinLop.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = leftJoinLop.right();
        final Schema rightInputSchema = rightInput.schema();

        final List<ColumnReference> leftInputProjection = new ArrayList<>();
        final List<ColumnReference> rightInputProjection = new ArrayList<>();
        for (ColumnReference column : newProjection) {
            if (leftInputSchema.contains(column)) {
                leftInputProjection.add(column);
            } else if (rightInputSchema.contains(column)) {
                rightInputProjection.add(column);
            }
        }

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(rightInputProjection).apply(rightInput);
        final LeftJoinLop transformedJoin = new LeftJoinLop(transformedLeftInput,
                transformedRightInput,
                leftJoinLop.joinSpecification(),
                leftJoinOutputSchema(transformedLeftInput.schema(), transformedRightInput.schema()));

        if (newProjection.size() != projection.size()) {
            return new ProjectionLop(transformedJoin, projection);
        } else {
            return transformedJoin;
        }
    }

    @Override
    public LogicalOperator visit(LimitLop limitLop) {
        return limitLop.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(MapLop mapNode) {
        final List<ColumnReference> projectionOnMapInput = restrictProjectionToAvailableColumns(mapNode.input(),
                extendProjectionWithColumnsRequiredForMap(
                        mapNode));

        final LogicalOperator transformedInput = new ProjectionPushDown(projectionOnMapInput).apply(mapNode.input());

        final LogicalOperator transformedMapNode = new MapLop(transformedInput,
                mapNode.expressions(),
                mapNode.expressionsOutputNames());

        return createProjectionIfNecessary(transformedMapNode);
    }

    private List<ColumnReference> extendProjectionWithColumnsRequiredForMap(MapLop mapNode) {
        final List<ColumnReference> columnsRequiredForMap = mapNode.expressions()
                .stream()
                .flatMap(ValueExpression::variables)
                .collect(toList());

        return merge(projection, mapNode.input().schema(), columnsRequiredForMap);
    }

    @Override
    public LogicalOperator visit(ProjectionLop projectionNode) {
        // We ignore this projection, because it can only be broader than the projection we're using
        return apply(projectionNode.input());
    }

    @Override
    public LogicalOperator visit(SemiJoinLop semiJoin) {
        final LogicalOperator leftInput = semiJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = semiJoin.right();
        final Schema rightInputSchema = rightInput.schema();

        final List<ColumnReference> columnsRequiredForPredicate = semiJoin.predicate().variables().collect(toList());
        final List<ColumnReference> allRequiredColumns = union(projection, columnsRequiredForPredicate);

        final List<ColumnReference> leftInputProjection = new ArrayList<>();
        final List<ColumnReference> rightInputProjection = new ArrayList<>();
        for (ColumnReference projectedColumn : allRequiredColumns) {
            if (leftInputSchema.contains(projectedColumn)) {
                leftInputProjection.add(projectedColumn);
            } else if (rightInputSchema.contains(projectedColumn)) {
                rightInputProjection.add(projectedColumn);
            }
        }

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(rightInputProjection).apply(rightInput);
        final SemiJoinLop transformedJoin = new SemiJoinLop(
                transformedLeftInput,
                transformedRightInput,
                semiJoin.predicate(),
                semiJoin.ouputColumnName());

        if (transformedJoin.schema().numberOfAttributes() != projection.size()) {
            return new ProjectionLop(transformedJoin, projection);
        } else {
            return transformedJoin;
        }
    }

    private List<ColumnReference> union(List<ColumnReference> projection, List<ColumnReference> columnsRequiredForPredicate) {
        final List<ColumnReference> union = new ArrayList<>(projection);
        for (ColumnReference columnReference : columnsRequiredForPredicate) {
            if (!union.contains(columnReference)) {
                union.add(columnReference);
            }
        }
        return union;
    }

    @Override
    public LogicalOperator visit(SortLop sortLop) {
        return sortLop.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(SubqueryExecutionLop subqueryExecutionLop) {
        final LogicalOperator transformedInput = new ProjectionPushDown(
                restrictProjectionToAvailableColumns(subqueryExecutionLop.input(), projection)
        ).apply(subqueryExecutionLop.input());
        final SubqueryExecutionLop transformedNode = new SubqueryExecutionLop(transformedInput,
                subqueryExecutionLop.subquery());

        return createProjectionIfNecessary(transformedNode);
    }

    private List<ColumnReference> restrictProjectionToAvailableColumns(LogicalOperator node, List<ColumnReference> projection) {
        final Schema schema = node.schema();

        return projection.stream().filter(schema::contains).collect(toList());
    }

    private LogicalOperator createProjectionIfNecessary(LogicalOperator node) {
        if (node.schema().columnNames().size() != projection.size()) {
            return new ProjectionLop(node, projection);
        } else {
            return node;
        }
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        if (operator.schema().columnNames().size() != projection.size()) {
            return new ProjectionLop(operator, projection);
        } else {
            return operator;
        }
    }
}
