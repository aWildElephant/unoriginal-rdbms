package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.function.Function;

public interface LogicalOperator {

    Schema schema();

    LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer);

    <T> T accept(LopVisitor<T> visitor);
}
