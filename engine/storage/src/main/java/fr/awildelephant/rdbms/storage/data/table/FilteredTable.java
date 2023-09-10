package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.bitmap.Bitmap;
import fr.awildelephant.rdbms.storage.data.column.Column;
import fr.awildelephant.rdbms.storage.data.column.FilteredColumn;
import fr.awildelephant.rdbms.storage.data.record.FilteredMultipleColumnsIterator;
import fr.awildelephant.rdbms.storage.data.record.Record;

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

    public Table materialize() {
        final WriteableTable outputTable = TableFactory.simpleTable(wrappedTable.schema(), bitmap.cardinality());
        for (Record record : this) {
            outputTable.add(record);
        }
        return outputTable;
    }

    @Override
    public Iterator<Record> iterator() {
        return new FilteredMultipleColumnsIterator(wrappedTable.columns(), bitmap);
    }
}
