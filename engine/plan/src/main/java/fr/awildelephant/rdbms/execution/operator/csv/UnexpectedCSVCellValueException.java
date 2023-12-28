package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.execution.exception.PlanExecutionException;
import fr.awildelephant.rdbms.schema.Domain;

import java.time.format.DateTimeParseException;

public final class UnexpectedCSVCellValueException extends PlanExecutionException {

    private final String value;
    private final Domain domain;

    public UnexpectedCSVCellValueException(final String value, final Domain domain) {
        this.value = value;
        this.domain = domain;
    }

    public UnexpectedCSVCellValueException(String value, Domain domain, DateTimeParseException cause) {
        super(cause);

        this.value = value;
        this.domain = domain;
    }
}
