package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Optional;

public interface Aggregate {

    boolean outputIsNullable();

    ColumnReference outputName();

    Optional<ColumnReference> inputColumn();
}
