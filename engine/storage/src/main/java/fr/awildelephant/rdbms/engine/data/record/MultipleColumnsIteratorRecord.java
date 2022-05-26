package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.column.Column;

import java.util.List;

public final class MultipleColumnsIteratorRecord implements Record {

    private final List<? extends Column> columns;
    private int position;

    public MultipleColumnsIteratorRecord(List<? extends Column> columns) {
        this.columns = columns;
    }

    @Override
    public DomainValue get(int columnIndex) {
        return columns.get(columnIndex).get(position);
    }

    @Override
    public int size() {
        return columns.size();
    }

    @Override
    public Record materialize() {
        final int size = columns.size();
        final DomainValue[] values = new DomainValue[size];

        for (int i = 0; i < size; i++) {
            values[i] = columns.get(i).get(position);
        }

        return new Tuple(values);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean validPosition(int position) {
        return position < columns.get(0).size();
    }
}
