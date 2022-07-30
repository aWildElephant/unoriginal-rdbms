package fr.awildelephant.rdbms.execution;

public abstract class DefaultLopVisitor<T> implements LopVisitor<T> {

    @Override
    public T visit(AggregationLop aggregation) {
        return defaultVisit(aggregation);
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
    public T visit(CartesianProductLop cartesianProduct) {
        return defaultVisit(cartesianProduct);
    }

    @Override
    public T visit(DistinctLop distinct) {
        return defaultVisit(distinct);
    }

    @Override
    public T visit(FilterLop filter) {
        return defaultVisit(filter);
    }

    @Override
    public T visit(InnerJoinLop innerJoin) {
        return defaultVisit(innerJoin);
    }

    @Override
    public T visit(LeftJoinLop leftJoin) {
        return defaultVisit(leftJoin);
    }

    @Override
    public T visit(LimitLop limit) {
        return defaultVisit(limit);
    }

    @Override
    public T visit(MapLop map) {
        return defaultVisit(map);
    }

    @Override
    public T visit(ProjectionLop projection) {
        return defaultVisit(projection);
    }

    @Override
    public T visit(ScalarSubqueryLop scalarSubquery) {
        return defaultVisit(scalarSubquery);
    }

    @Override
    public T visit(DependentSemiJoinLop dependentSemiJoin) {
        return defaultVisit(dependentSemiJoin);
    }

    @Override
    public T visit(SemiJoinLop semiJoin) {
        return defaultVisit(semiJoin);
    }

    @Override
    public T visit(SortLop sort) {
        return defaultVisit(sort);
    }

    @Override
    public T visit(DependentJoinLop dependentJoin) {
        return defaultVisit(dependentJoin);
    }

    @Override
    public T visit(TableConstructorLop tableConstructor) {
        return defaultVisit(tableConstructor);
    }

    public abstract T defaultVisit(LogicalOperator operator);
}
