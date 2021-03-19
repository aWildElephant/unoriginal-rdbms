package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.Tuple;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static fr.awildelephant.rdbms.engine.data.record.Tuple.EMPTY_TUPLE;

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
    public Tuple next() {
        if (cursor >= size) {
            throw new NoSuchElementException();
        }

        cursor++;

        return EMPTY_TUPLE;
    }
}
