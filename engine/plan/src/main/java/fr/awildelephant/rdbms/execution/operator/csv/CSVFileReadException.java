package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.execution.exception.PlanExecutionException;

public final class CSVFileReadException extends PlanExecutionException {

    private final String filePath;

    public CSVFileReadException(final String filePath, final Throwable cause) {
        super(cause);
        this.filePath = filePath;
    }
}
