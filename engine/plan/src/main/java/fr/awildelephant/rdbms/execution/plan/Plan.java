package fr.awildelephant.rdbms.execution.plan;

import java.util.Set;

public final class Plan {

    private final Set<PlanStep> steps;
    private final String target;

    public Plan(Set<PlanStep> steps, String target) {
        this.steps = steps;
        this.target = target;
    }

    public PlanStep step(String id) {
        return steps.stream().filter(step -> step.key().equals(id)).findAny().get();
    }

    public Set<PlanStep> steps() {
        return steps;
    }

    public String target() {
        return target;
    }
}
