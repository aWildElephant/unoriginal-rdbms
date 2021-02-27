package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.engine.optimizer.util.FreeVariablesFunction;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LeftJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.SemiJoinLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.SubqueryExecutionLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.leftJoinOutputSchema;
import static java.util.stream.Collectors.toList;

// TODO:
// 1) Vérifier qu'on ne fait pas passer sous un noeud une colonne qui n'existe pas dans la branche (s_name)
// 2) Supprimer outerQuerySchema, il faut juste ignorer les colonnes qu'on ne connait pas dans le noeud, c'est probablement des colonnes de l'outer schema
// 3) Re-test
public final class ProjectionPushDown extends DefaultLopVisitor<LogicalOperator> {

    private final FreeVariablesFunction freeVariablesFunction = new FreeVariablesFunction();

    private final List<ColumnReference> projection;

    private ProjectionPushDown(List<ColumnReference> projection) {
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
                                       innerJoinOutputSchema(transformedLeftInput.schema(),
                                                             transformedRightInput.schema()));
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

        if (projection.size() > newProjection.size()) {
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
                                                              innerJoinOutputSchema(transformedLeftInput.schema(),
                                                                                    transformedRightInput.schema()));

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
                                                            leftJoinOutputSchema(transformedLeftInput.schema(),
                                                                                 transformedRightInput.schema()));

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
    public LogicalOperator visit(SortLop sortLop) {
        return sortLop.transformInputs(this);
    }

    @Override
    public LogicalOperator visit(SubqueryExecutionLop subqueryExecution) {
        final LogicalOperator subquery = subqueryExecution.subquery();

        final List<ColumnReference> inputProjection
                = restrictProjectionToAvailableColumns(subqueryExecution.input(), projection);
        inputProjection.addAll(freeVariablesFunction.apply(subquery));

        final LogicalOperator transformedInput
                = new ProjectionPushDown(inputProjection).apply(subqueryExecution.input());

        final List<ColumnReference> subqueryProjection = restrictProjectionToAvailableColumns(subquery, projection);

        final LogicalOperator transformedSubquery = new ProjectionPushDown(subqueryProjection).apply(subquery);

        final SubqueryExecutionLop transformedNode = new SubqueryExecutionLop(transformedInput, transformedSubquery);

        return createProjectionIfNecessary(transformedNode);
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
