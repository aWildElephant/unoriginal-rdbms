package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public interface Plan {

    Schema schema();

    <T> T accept(PlanVisitor<T> visitor);
}
