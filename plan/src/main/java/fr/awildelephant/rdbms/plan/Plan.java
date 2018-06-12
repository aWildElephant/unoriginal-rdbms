package fr.awildelephant.rdbms.plan;

public interface Plan {

    <T> T accept(PlanVisitor<T> visitor);
}
