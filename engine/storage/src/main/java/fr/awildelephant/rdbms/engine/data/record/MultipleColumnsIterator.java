package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.column.Column;

import java.util.Iterator;
import java.util.List;

public final class MultipleColumnsIterator implements Iterator<Record> {

    private final List<Column> columns;
    private int cursor;

    public MultipleColumnsIterator(List<Column> columns) {
        this.columns = columns;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor < columns.get(0).size();
    }

    @Override
    public Record next() {
        final DomainValue[] values = new DomainValue[columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            values[i] = columns.get(i).get(cursor);
        }

        final Record record = new Record(values);

        cursor++;

        return record;
    }
}
