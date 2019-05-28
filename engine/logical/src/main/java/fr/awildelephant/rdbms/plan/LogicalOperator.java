package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

public interface LogicalOperator {

    Schema schema();

    <T> T accept(LopVisitor<T> visitor);
}
