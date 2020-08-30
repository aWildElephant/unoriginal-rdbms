package fr.awildelephant.rdbms.engine.operators.join;

import fr.awildelephant.rdbms.engine.data.record.Record;

public final class JoinRow {

    private final Record leftRow;
    private final Record rightRow;

    public JoinRow(Record leftRow, Record rightRow) {
        this.leftRow = leftRow;
        this.rightRow = rightRow;
    }

    public Record leftRow() {
        return leftRow;
    }

    public Record rightRow() {
        return rightRow;
    }
}
