package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.storage.data.table.Table;

import java.util.function.Function;

public interface PlanExecutor extends Function<Plan, Table> {
}
