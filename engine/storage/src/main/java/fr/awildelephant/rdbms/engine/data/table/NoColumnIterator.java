package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.record.Record;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static fr.awildelephant.rdbms.engine.data.record.Record.EMPTY_RECORD;

public final class NoColumnIterator implements Iterator<Record> {

    private final int size;
    private int cursor;

    public NoColumnIterator(int size) {
        this.size = size;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor < size;
    }

    @Override
    public Record next() {
        if (cursor >= size) {
            throw new NoSuchElementException();
        }

        cursor++;

        return EMPTY_RECORD;
    }
}
