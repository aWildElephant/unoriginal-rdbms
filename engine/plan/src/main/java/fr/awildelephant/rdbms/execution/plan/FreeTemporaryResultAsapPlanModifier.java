package fr.awildelephant.rdbms.execution.plan;

import fr.awildelephant.rdbms.execution.operator.FreeTemporaryResultOperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class FreeTemporaryResultAsapPlanModifier implements PlanModifier {

    @Override
    public Plan apply(Plan plan) {
        final List<PlanStep> steps = plan.steps();
        if (steps.size() == 1) {
            return plan;
        }

        final String targetKey = plan.targetKey();

        final Set<String> intermediateResultKeys = steps.stream().map(PlanStep::key)
                .filter(key -> !targetKey.equals(key))
                .collect(Collectors.toSet());

        final List<PlanStep> resultingSteps = new ArrayList<>();

        resultingSteps.add(steps.getLast());
        for (int i = steps.size() - 2; i >= 0; i--) {
            final PlanStep step = steps.get(i);

            final Set<String> canBeFreed = step.dependencies().stream()
                    .filter(intermediateResultKeys::contains)
                    .collect(Collectors.toUnmodifiableSet());

            if (!canBeFreed.isEmpty()) {
                final String key = "free-after-" + step.key();
                /* FIXME: other nodes might use the result we're freeing.
                          Not having them in the dependencies of this step might be an issue if we change the executor. */
                resultingSteps.add(new PlanStep(key, Set.of(step.key()), new FreeTemporaryResultOperator(canBeFreed)));
            }
            resultingSteps.add(step);
        }

        Collections.reverse(resultingSteps);

        return new Plan(resultingSteps, targetKey);
    }
}
