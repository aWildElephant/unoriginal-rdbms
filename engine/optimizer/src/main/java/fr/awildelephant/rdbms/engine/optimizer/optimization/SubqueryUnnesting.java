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
import fr.awildelephant.rdbms.plan.SubqueryExecutionLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.schema.Schema;

import static java.util.stream.Collectors.toList;

// TODO: ça ne fait rien pour l'instant
public final class SubqueryUnnesting implements LopVisitor<LogicalOperator> {

    @Override
    public LogicalOperator visit(AggregationLop aggregationNode) {
        return new AggregationLop(apply(aggregationNode.input()), aggregationNode.aggregates());
    }

    @Override
    public LogicalOperator visit(AliasLop alias) {
        return new AliasLop(apply(alias.input()), alias.alias());
    }

    @Override
    public LogicalOperator visit(BaseTableLop baseTable) {
        return baseTable;
    }

    @Override
    public LogicalOperator visit(BreakdownLop breakdownNode) {
        return new BreakdownLop(apply(breakdownNode.input()), breakdownNode.breakdowns());
    }

    @Override
    public LogicalOperator visit(CartesianProductLop cartesianProductNode) {
        return new CartesianProductLop(apply(cartesianProductNode.leftInput()),
                                       apply(cartesianProductNode.rightInput()),
                                       cartesianProductNode.schema());
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
        return new FilterLop(apply(filter.input()), filter.filter());
    }

    private CartesianProductLop cartesianProduct(LogicalOperator left, LogicalOperator right) {
        final Schema outputSchema = joinOutputSchema(left.schema(), right.schema());

        return new CartesianProductLop(left, right, outputSchema);
    }

    private static Schema joinOutputSchema(Schema leftSchema, Schema rightSchema) {
        return leftSchema
                .extend(rightSchema.columnNames().stream().map(rightSchema::column).collect(toList()));
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoinLop) {
        final LogicalOperator left = apply(innerJoinLop.left());
        final LogicalOperator right = apply(innerJoinLop.right());

        return new InnerJoinLop(left, right, innerJoinLop.joinSpecification(), innerJoinLop.schema());
    }

    @Override
    public LogicalOperator visit(LimitLop limitLop) {
        return new LimitLop(apply(limitLop.input()), limitLop.limit());
    }

    @Override
    public LogicalOperator visit(MapLop mapNode) {
        return new MapLop(apply(mapNode.input()), mapNode.expressions(), mapNode.expressionsOutputNames());
    }

    @Override
    public LogicalOperator visit(ProjectionLop projectionNode) {
        return new ProjectionLop(apply(projectionNode.input()), projectionNode.outputColumns());
    }

    @Override
    public LogicalOperator visit(ScalarSubqueryLop scalarSubquery) {
        return new ScalarSubqueryLop(apply(scalarSubquery.input()), scalarSubquery.id());
    }

    @Override
    public LogicalOperator visit(SubqueryExecutionLop scalarSubquery) {
        return new SubqueryExecutionLop(apply(scalarSubquery.input()), apply(scalarSubquery.subquery()));
    }

    @Override
    public LogicalOperator visit(SortLop sortLop) {
        return new SortLop(apply(sortLop.input()), sortLop.sortSpecificationList());
    }

    @Override
    public LogicalOperator visit(TableConstructorLop tableConstructor) {
        return tableConstructor;
    }
}
