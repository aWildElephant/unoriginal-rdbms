package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;

public interface Aggregate {

    boolean outputIsNullable();

    ColumnReference outputName();
}
