package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.plan.Plan;

import java.util.function.Function;

public interface PlanExecutor extends Function<Plan, Table> {
}
