package fr.awildelephant.rdbms.execution.plan;

import fr.awildelephant.rdbms.operator.logical.LogicalOperator;

import java.util.function.Function;

public final class PlanFactory implements Function<LogicalOperator, Plan> {

    @Override
    public Plan apply(LogicalOperator logicalOperator) {
        final Plan basicPlan = new BasicPlanGenerator(logicalOperator).build();

        return new FreeTemporaryResultAsapPlanModifier().apply(basicPlan);
    }
}
