package fr.awildelephant.rdbms.execution.plan;

import java.util.function.UnaryOperator;

public interface PlanModifier extends UnaryOperator<Plan> {
}
