package fr.awildelephant.rdbms.execution.plan;

import java.util.List;

public final class Plan {

    private final List<PlanStep> steps;
    private final String targetKey;

    public Plan(List<PlanStep> steps, String targetKey) {
        this.steps = steps;
        this.targetKey = targetKey;
    }

    public List<PlanStep> steps() {
        return steps;
    }

    public String targetKey() {
        return targetKey;
    }
}
