package fr.awildelephant.rdbms.plan;

import java.util.function.Function;

public interface LopVisitor<T> extends Function<LogicalOperator, T> {

    default T apply(LogicalOperator plan) {
        return plan.accept(this);
    }

    T visit(AggregationLop aggregationNode);

    T visit(AliasLop alias);

    T visit(BaseTableLop baseTable);

    T visit(BreakdownLop breakdownNode);

    T visit(CartesianProductLop cartesianProductNode);

    T visit(CollectLop collectNode);

    T visit(DistinctLop distinctNode);

    T visit(FilterLop filter);

    T visit(InnerJoinLop innerJoinLop);

    T visit(LimitLop limitLop);

    T visit(MapLop mapNode);

    T visit(ProjectionLop projectionNode);

    T visit(ScalarSubqueryLop scalarSubquery);

    T visit(SortLop sortLop);

    T visit(TableConstructorLop tableConstructor);
}
