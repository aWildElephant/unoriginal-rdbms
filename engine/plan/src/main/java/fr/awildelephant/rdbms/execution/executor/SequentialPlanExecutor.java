package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.execution.plan.PlanStep;

public final class SequentialPlanExecutor implements PlanExecutor {

    @Override
    public Table apply(Storage storage, Plan plan) {
        final TemporaryStorage temporaryStorage = new TemporaryStorage(storage);
        for (PlanStep step : plan.steps()) {
            execute(step, temporaryStorage);
        }

        final Table result = temporaryStorage.get(plan.targetKey());

        if (result == null) {
            throw new IllegalStateException(); // TODO: proper error
        }

        return result;
    }

    private void execute(PlanStep step, TemporaryStorage temporaryStorage) {
        temporaryStorage.storeTemporaryResult(step.key(), step.operator().compute(temporaryStorage));
    }
}
