package fr.awildelephant.rdbms.execution.aggregation;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Optional;

public interface Aggregate {

    boolean outputIsNullable();

    Optional<ColumnReference> inputColumn();

    ColumnReference outputColumn();
}
