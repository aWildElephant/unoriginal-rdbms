package fr.awildelephant.rdbms.storage.constraint;

import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.exception.NotNullConstraintViolationError;

public class NotNullChecker implements ConstraintChecker {

    private final int columnIndex;
    private final String columnName;

    public NotNullChecker(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    @Override
    public void check(Record record) {
        if (record.get(columnIndex).isNull()) {
            throw new NotNullConstraintViolationError(columnName);
        }
    }
}
