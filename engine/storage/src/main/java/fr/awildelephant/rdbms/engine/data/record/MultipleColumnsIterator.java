package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.engine.data.column.Column;

import java.util.Iterator;
import java.util.List;

public final class MultipleColumnsIterator implements Iterator<Record> {

    private final MultipleColumnsIteratorRecord record;

    private int cursor;

    public MultipleColumnsIterator(List<Column> columns) {
        record = new MultipleColumnsIteratorRecord(columns);
        cursor = -1;
    }

    @Override
    public boolean hasNext() {
        return record.validPosition(cursor + 1);
    }

    @Override
    public Record next() {
        record.setPosition(++cursor);

        return record;
    }
}
