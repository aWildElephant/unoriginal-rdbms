package fr.awildelephant.rdbms.execution.plan;

import fr.awildelephant.rdbms.execution.operator.Operator;

import java.util.Set;

public class PlanStep {

    private final String key;
    private final Set<String> dependencies;
    private final Operator operator;

    public PlanStep(String key, Set<String> dependencies, Operator operator) {
        this.key = key;
        this.dependencies = dependencies;
        this.operator = operator;
    }

    public String key() {
        return key;
    }

    public Set<String> dependencies() {
        return dependencies;
    }

    public Operator operator() {
        return operator;
    }
}
