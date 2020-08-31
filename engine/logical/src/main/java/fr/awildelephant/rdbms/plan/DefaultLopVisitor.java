package fr.awildelephant.rdbms.plan;

public abstract class DefaultLopVisitor<T> implements LopVisitor<T> {

    @Override
    public T visit(AggregationLop aggregationNode) {
        return defaultVisit(aggregationNode);
    }

    @Override
    public T visit(AliasLop alias) {
        return defaultVisit(alias);
    }

    @Override
    public T visit(BaseTableLop baseTable) {
        return defaultVisit(baseTable);
    }

    @Override
    public T visit(CartesianProductLop cartesianProductNode) {
        return defaultVisit(cartesianProductNode);
    }

    @Override
    public T visit(DistinctLop distinctNode) {
        return defaultVisit(distinctNode);
    }

    @Override
    public T visit(FilterLop filter) {
        return defaultVisit(filter);
    }

    @Override
    public T visit(InnerJoinLop innerJoinLop) {
        return defaultVisit(innerJoinLop);
    }

    @Override
    public T visit(LeftJoinLop leftJoinLop) {
        return defaultVisit(leftJoinLop);
    }

    @Override
    public T visit(LimitLop limitLop) {
        return defaultVisit(limitLop);
    }

    @Override
    public T visit(MapLop mapNode) {
        return defaultVisit(mapNode);
    }

    @Override
    public T visit(ProjectionLop projectionNode) {
        return defaultVisit(projectionNode);
    }

    @Override
    public T visit(ScalarSubqueryLop scalarSubquery) {
        return defaultVisit(scalarSubquery);
    }

    @Override
    public T visit(SemiJoinLop semiJoin) {
        return defaultVisit(semiJoin);
    }

    @Override
    public T visit(SortLop sortLop) {
        return defaultVisit(sortLop);
    }

    @Override
    public T visit(SubqueryExecutionLop subqueryExecutionLop) {
        return defaultVisit(subqueryExecutionLop);
    }

    @Override
    public T visit(TableConstructorLop tableConstructor) {
        return defaultVisit(tableConstructor);
    }

    public abstract T defaultVisit(LogicalOperator operator);
}
