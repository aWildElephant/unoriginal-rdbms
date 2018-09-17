package fr.awildelephant.rdbms.engine.constraint;

import fr.awildelephant.rdbms.engine.data.record.Record;

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
            throw new IllegalArgumentException("Cannot insert NULL in not-null column " + columnName);
        }
    }
}
