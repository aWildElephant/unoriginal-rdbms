package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.engine.bitmap.Bitmap;
import fr.awildelephant.rdbms.engine.data.column.Column;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class FilteredMultipleColumnsIterator implements Iterator<Record> {

    private final MultipleColumnsIteratorRecord record;
    private final Bitmap bitmap;

    private int cursor;

    public FilteredMultipleColumnsIterator(List<? extends Column> columns, Bitmap bitmap) {
        this.record = new MultipleColumnsIteratorRecord(columns);
        this.bitmap = bitmap;
        this.cursor = -1;
    }

    @Override
    public boolean hasNext() {
        return bitmap.nextSetBit(cursor + 1) >= 0;
    }

    @Override
    public Record next() {
        cursor = bitmap.nextSetBit(cursor + 1);
        if (cursor < 0) {
            throw new NoSuchElementException();
        }

        record.setPosition(cursor);
        return record;
    }
}
