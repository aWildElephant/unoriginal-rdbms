package fr.awildelephant.rdbms.plan;

import java.util.function.Function;

public interface LopVisitor<T> extends Function<LogicalOperator, T> {

    default T apply(LogicalOperator plan) {
        return plan.accept(this);
    }

    T visit(AggregationLop aggregation);

    T visit(AliasLop alias);

    T visit(BaseTableLop baseTable);

    T visit(CartesianProductLop cartesianProduct);

    T visit(DependentJoinLop dependentJoin);

    T visit(DependentSemiJoinLop dependentSemiJoin);

    T visit(DistinctLop distinct);

    T visit(FilterLop filter);

    T visit(InnerJoinLop innerJoin);

    T visit(LeftJoinLop leftJoin);

    T visit(LimitLop limit);

    T visit(MapLop map);

    T visit(ProjectionLop projection);

    T visit(ScalarSubqueryLop scalarSubquery);

    T visit(SemiJoinLop semiJoin);

    T visit(SortLop sort);

    T visit(TableConstructorLop tableConstructor);
}
