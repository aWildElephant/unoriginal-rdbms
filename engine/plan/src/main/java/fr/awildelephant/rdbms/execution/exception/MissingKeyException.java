package fr.awildelephant.rdbms.execution.exception;

public class MissingKeyException extends PlanExecutionException {

    private final String stepKey;

    public MissingKeyException(String stepKey) {
        this.stepKey = stepKey;
    }

    @Override
    public String getMessage() {
        return "Key '" + stepKey + "' is missing from temporary storage";
    }
}
