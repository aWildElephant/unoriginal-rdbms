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

// TODO:
// 1) Vérifier qu'on ne fait pas passer sous un noeud une colonne qui n'existe pas dans la branche (s_name)
// 2) Supprimer outerQuerySchema, il faut juste ignorer les colonnes qu'on ne connait pas dans le noeud, c'est probablement des colonnes de l'outer schema
// 3) Re-test
public final class ProjectionPushDown extends DefaultLopVisitor<LogicalOperator> {

    private final List<ColumnReference> projection;

    private ProjectionPushDown(List<ColumnReference> projection) {
        this.projection = projection;
    }

    public static LogicalOperator pushDownProjections(LogicalOperator operator) {
        return new ProjectionPushDown(operator.schema().columnNames()).apply(operator);
    }

    @Override
    public LogicalOperator visit(AggregationLop aggregation) {
        final List<ColumnReference> extendedProjection
                = extendProjectionWithColumnsRequiredForAggregation(aggregation);

        final LogicalOperator transformedInput = new ProjectionPushDown(
                restrictProjectionToAvailableColumns(aggregation.input(), extendedProjection)
        ).apply(aggregation.input());

        final LogicalOperator transformedNode = new AggregationLop(transformedInput, aggregation.breakdowns(),
                                                                   aggregation.aggregates());

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
            if (inputSchema.contains(column)) {
                final ColumnReference normalizedColumn = inputSchema.normalize(column);
                if (!mergeResult.contains(normalizedColumn)) {
                    mergeResult.add(normalizedColumn);
                }
            }
        }
        return mergeResult;
    }

    @Override
    public LogicalOperator visit(CartesianProductLop cartesianProduct) {
        final LogicalOperator leftInput = cartesianProduct.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = cartesianProduct.right();
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
                                       innerJoinOutputSchema(transformedLeftInput.schema(),
                                                             transformedRightInput.schema()));
    }

    @Override
    public LogicalOperator visit(DistinctLop distinct) {
        return distinct.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(FilterLop filterNode) {
        final List<ColumnReference> columnsRequiredForFilter = filterNode.filter().variables().collect(toList());

        final List<ColumnReference> newProjection = merge(projection,
                                                          filterNode.input().schema(),
                                                          columnsRequiredForFilter);

        final LogicalOperator transformedFilter = filterNode.transformInputs(new ProjectionPushDown(newProjection));

        if (projection.size() > newProjection.size()) {
            return new ProjectionLop(transformedFilter, projection);
        } else {
            return transformedFilter;
        }
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoin) {
        final List<ColumnReference> newProjection = merge(projection,
                                                          innerJoin.schema(),
                                                          innerJoin.joinSpecification()
                                                                  .variables()
                                                                  .collect(toList()));

        final LogicalOperator leftInput = innerJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = innerJoin.right();
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
                                                              innerJoin.joinSpecification(),
                                                              innerJoinOutputSchema(transformedLeftInput.schema(),
                                                                                    transformedRightInput.schema()));

        if (newProjection.size() != projection.size()) {
            return new ProjectionLop(transformedJoin, projection);
        } else {
            return transformedJoin;
        }
    }

    @Override
    public LogicalOperator visit(LeftJoinLop leftJoin) {
        final List<ColumnReference> newProjection = merge(projection,
                                                          leftJoin.schema(),
                                                          leftJoin.joinSpecification()
                                                                  .variables()
                                                                  .collect(toList()));

        final LogicalOperator leftInput = leftJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = leftJoin.right();
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
                                                            leftJoin.joinSpecification(),
                                                            leftJoinOutputSchema(transformedLeftInput.schema(),
                                                                                 transformedRightInput.schema()));

        if (newProjection.size() != projection.size()) {
            return new ProjectionLop(transformedJoin, projection);
        } else {
            return transformedJoin;
        }
    }

    @Override
    public LogicalOperator visit(LimitLop limit) {
        return limit.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(MapLop map) {
        final List<ColumnReference> projectionOnMapInput = restrictProjectionToAvailableColumns(map.input(),
                                                                                                extendProjectionWithColumnsRequiredForMap(
                                                                                                        map));

        final LogicalOperator transformedInput = new ProjectionPushDown(projectionOnMapInput).apply(map.input());

        final LogicalOperator transformedMapNode = new MapLop(transformedInput,
                                                              map.expressions(),
                                                              map.expressionsOutputNames());

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
    public LogicalOperator visit(ProjectionLop projection) {
        // We ignore this projection, because it can only be broader than the projection we're using
        return apply(projection.input());
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
                semiJoin.outputColumnName());

        if (transformedJoin.schema().numberOfAttributes() != projection.size()) {
            return new ProjectionLop(transformedJoin, projection);
        } else {
            return transformedJoin;
        }
    }

    private List<ColumnReference> union(List<ColumnReference> projection,
                                        List<ColumnReference> columnsRequiredForPredicate) {
        final List<ColumnReference> union = new ArrayList<>(projection);
        for (ColumnReference columnReference : columnsRequiredForPredicate) {
            if (!union.contains(columnReference)) {
                union.add(columnReference);
            }
        }
        return union;
    }

    @Override
    public LogicalOperator visit(SortLop sort) {
        return sort.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(DependentJoinLop dependentJoin) {
        final List<ColumnReference> newProjection = merge(projection,
                                                          dependentJoin.schema(),
                                                          dependentJoin.predicate().variables().collect(toList()));

        final LogicalOperator leftInput = dependentJoin.left();
        final Schema leftInputSchema = leftInput.schema();
        final LogicalOperator rightInput = dependentJoin.right();
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

        return new DependentJoinLop(transformedLeftInput, transformedRightInput, dependentJoin.predicate());
    }

    private List<ColumnReference> restrictProjectionToAvailableColumns(LogicalOperator node,
                                                                       List<ColumnReference> projection) {
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
