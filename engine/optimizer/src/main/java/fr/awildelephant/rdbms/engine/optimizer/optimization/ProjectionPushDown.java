package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.function.VariableCollector;
import fr.awildelephant.rdbms.operator.logical.AggregationLop;
import fr.awildelephant.rdbms.operator.logical.AliasLop;
import fr.awildelephant.rdbms.operator.logical.CartesianProductLop;
import fr.awildelephant.rdbms.operator.logical.DefaultLopVisitor;
import fr.awildelephant.rdbms.operator.logical.DependentJoinLop;
import fr.awildelephant.rdbms.operator.logical.DistinctLop;
import fr.awildelephant.rdbms.operator.logical.FilterLop;
import fr.awildelephant.rdbms.operator.logical.InnerJoinLop;
import fr.awildelephant.rdbms.operator.logical.LeftJoinLop;
import fr.awildelephant.rdbms.operator.logical.LimitLop;
import fr.awildelephant.rdbms.operator.logical.LogicalOperator;
import fr.awildelephant.rdbms.operator.logical.MapLop;
import fr.awildelephant.rdbms.operator.logical.ProjectionLop;
import fr.awildelephant.rdbms.operator.logical.SemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.SortLop;
import fr.awildelephant.rdbms.operator.logical.aggregation.Aggregate;
import fr.awildelephant.rdbms.operator.logical.alias.ReversibleAlias;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fr.awildelephant.rdbms.execution.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.execution.JoinOutputSchemaFactory.leftJoinOutputSchema;
import static java.util.stream.Collectors.toList;

// TODO:
// 1) Vérifier qu'on ne fait pas passer sous un noeud une colonne qui n'existe pas dans la branche (s_name)
// 2) Supprimer outerQuerySchema, il faut juste ignorer les colonnes qu'on ne connait pas dans le noeud, c'est probablement des colonnes de l'outer schema
// 3) Re-test
public final class ProjectionPushDown extends DefaultLopVisitor<LogicalOperator> {

    private final VariableCollector variableCollector;
    private final List<ColumnReference> projection;

    public ProjectionPushDown(VariableCollector variableCollector, List<ColumnReference> projection) {
        this.variableCollector = variableCollector;
        this.projection = projection;
    }

    @Override
    public LogicalOperator visit(AggregationLop aggregation) {
        final List<ColumnReference> extendedProjection
                = extendProjectionWithColumnsRequiredForAggregation(aggregation);

        final LogicalOperator transformedInput = new ProjectionPushDown(
                variableCollector, restrictProjectionToAvailableColumns(aggregation.input(), extendedProjection)
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
        final ReversibleAlias alias = aliasNode.alias();

        final List<ColumnReference> unaliasedProjection = projection.stream().map(alias::unalias)
                .collect(toList());

        final LogicalOperator transformedInput = new ProjectionPushDown(variableCollector, unaliasedProjection).apply(aliasNode.input());
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

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(variableCollector, leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(variableCollector, rightInputProjection).apply(rightInput);

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
        final List<ColumnReference> columnsRequiredForFilter = variables(filterNode.filter());

        final List<ColumnReference> newProjection = merge(projection,
                filterNode.input().schema(),
                columnsRequiredForFilter);

        final LogicalOperator transformedFilter = filterNode.transformInputs(new ProjectionPushDown(variableCollector, newProjection));

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
                variables(innerJoin.joinSpecification()));

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

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(variableCollector, leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(variableCollector, rightInputProjection).apply(rightInput);
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
                variables(leftJoin.joinSpecification()));

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

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(variableCollector, leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(variableCollector, rightInputProjection).apply(rightInput);
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

        final LogicalOperator transformedInput = new ProjectionPushDown(variableCollector, projectionOnMapInput).apply(map.input());

        final LogicalOperator transformedMapNode = new MapLop(transformedInput,
                map.expressions(),
                map.expressionsOutputNames());

        return createProjectionIfNecessary(transformedMapNode);
    }

    private List<ColumnReference> extendProjectionWithColumnsRequiredForMap(MapLop mapNode) {
        final List<ColumnReference> columnsRequiredForMap = variables(mapNode.expressions());

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

        final List<ColumnReference> columnsRequiredForPredicate = variables(semiJoin.predicate());
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

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(variableCollector, leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(variableCollector, rightInputProjection).apply(rightInput);
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
        final List<ColumnReference> newProjection = merge(projection, dependentJoin.schema(),
                variables(dependentJoin.predicate()));

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

        final LogicalOperator transformedLeftInput = new ProjectionPushDown(variableCollector, leftInputProjection).apply(leftInput);
        final LogicalOperator transformedRightInput = new ProjectionPushDown(variableCollector, rightInputProjection).apply(rightInput);

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

    private List<ColumnReference> variables(ValueExpression valueExpression) {
        return variableCollector.apply(valueExpression);
    }

    private List<ColumnReference> variables(List<ValueExpression> valueExpressions) {
        return variableCollector.apply(valueExpressions);
    }
}
