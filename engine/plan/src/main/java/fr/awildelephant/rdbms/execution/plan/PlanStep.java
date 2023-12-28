package fr.awildelephant.rdbms.execution.plan;

import fr.awildelephant.rdbms.execution.operator.Operator;

import java.util.Set;

public record PlanStep(String key, Set<String> dependencies, Operator operator) {

}
