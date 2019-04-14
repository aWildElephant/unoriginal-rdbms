package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.Domain;

public interface Aggregate {

    boolean outputIsNullable();

    String outputName();

    Domain outputType();
}
