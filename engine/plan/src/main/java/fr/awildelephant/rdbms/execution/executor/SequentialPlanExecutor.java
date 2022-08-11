package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.execution.plan.PlanStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SequentialPlanExecutor implements PlanExecutor {

    private static final Logger LOGGER = LogManager.getLogger();

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
        final String stepKey = step.key();

        LOGGER.info("Executing step {} operator={} dependencies={}", stepKey, step.operator().getClass().getSimpleName(), step.dependencies());

        temporaryStorage.storeTemporaryResult(stepKey, step.operator().compute(temporaryStorage));

        LOGGER.info("Done executing step {}", stepKey);
    }
}
