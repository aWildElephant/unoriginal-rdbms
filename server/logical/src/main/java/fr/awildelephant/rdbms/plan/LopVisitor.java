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

    T visit(DistinctLop distinctNode);

    T visit(ExplicitTableLop explicitTable);

    T visit(MapLop mapNode);

    T visit(ProjectionLop projectionNode);
}
