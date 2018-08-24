package fr.awildelephant.rdbms.plan;

import java.util.function.Function;

public interface PlanVisitor<T> extends Function<Plan, T> {

    default T apply(Plan plan) {
        return plan.accept(this);
    }

    T visit(AliasNode alias);

    T visit(BaseTable baseTable);

    T visit(DistinctNode distinctNode);

    T visit(MapNode mapNode);

    T visit(ProjectionNode projectionNode);
}
