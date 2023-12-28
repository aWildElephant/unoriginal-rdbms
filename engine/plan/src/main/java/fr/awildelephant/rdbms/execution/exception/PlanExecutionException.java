package fr.awildelephant.rdbms.execution.exception;

public abstract class PlanExecutionException extends RuntimeException {

    protected PlanExecutionException() {

    }

    protected PlanExecutionException(final Throwable cause) {
        super(cause);
    }
}
