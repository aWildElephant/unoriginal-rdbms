package fr.awildelephant.rdbms.execution.exception;

public class KeyAlreadyExistsException extends PlanExecutionException {

    private final String stepKey;

    public KeyAlreadyExistsException(final String stepKey) {
        this.stepKey = stepKey;
    }

    @Override
    public String getMessage() {
        return "Key '" + stepKey + "' already exists";
    }
}
