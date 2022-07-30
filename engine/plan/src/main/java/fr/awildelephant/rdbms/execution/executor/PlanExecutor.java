package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.plan.Plan;

import java.util.function.BiFunction;

public interface PlanExecutor extends BiFunction<Storage, Plan, Table> {
}
