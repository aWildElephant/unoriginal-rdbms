package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.bitmap.Bitmap;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.column.FilteredColumn;
import fr.awildelephant.rdbms.engine.data.record.FilteredMultipleColumnsIterator;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FilteredTable implements Table {

    private final Table wrappedTable;
    private final Bitmap bitmap;

    public FilteredTable(Table wrappedTable, Bitmap bitmap) {
        this.wrappedTable = wrappedTable;
        this.bitmap = bitmap;
    }

    @Override
    public Schema schema() {
        return wrappedTable.schema();
    }

    @Override
    public void add(Record newRecord) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAll(Collection<Record> newRecords) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int numberOfTuples() {
        return bitmap.cardinality();
    }

    @Override
    public boolean isEmpty() {
        return bitmap.nextSetBit(0) < 0;
    }

    @Override
    public List<Column> columns() {
        return wrappedTable.columns().stream().<Column>map(column -> new FilteredColumn(column, bitmap)).toList();
    }

    @Override
    public Iterator<Record> iterator() {
        return new FilteredMultipleColumnsIterator(wrappedTable.columns(), bitmap);
    }
}
