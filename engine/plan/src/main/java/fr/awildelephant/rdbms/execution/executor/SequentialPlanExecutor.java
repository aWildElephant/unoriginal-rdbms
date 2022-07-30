package fr.awildelephant.rdbms.execution.executor;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.plan.Plan;
import fr.awildelephant.rdbms.execution.plan.PlanStep;

import java.util.Set;
import java.util.stream.Collectors;

public final class SequentialPlanExecutor implements PlanExecutor {

    @Override
    public Table apply(Storage storage, Plan plan) {
        final PlanStep targetStep = plan.step(plan.target());

        final TemporaryStorage temporaryStorage = new TemporaryStorage(storage);

        Set<PlanStep> ready;
        do {
            ready = getReadySteps(plan, temporaryStorage);
            ready.forEach(step -> execute(step, temporaryStorage));
        } while (!ready.isEmpty());

        return temporaryStorage.get(targetStep.key());
    }

    private void execute(PlanStep step, TemporaryStorage temporaryStorage) {
        temporaryStorage.allocate(step.key(), step.operator().compute(temporaryStorage));
    }

    private Set<PlanStep> getReadySteps(Plan plan, TemporaryStorage temporaryStorage) {
        return plan.steps().stream()
                .filter(step -> isReady(step, temporaryStorage))
                .collect(Collectors.toSet());
    }

    private boolean isReady(PlanStep step, TemporaryStorage temporaryStorage) {
        return !temporaryStorage.contains(step.key()) && step.dependencies().stream().allMatch(temporaryStorage::contains);
    }
}
