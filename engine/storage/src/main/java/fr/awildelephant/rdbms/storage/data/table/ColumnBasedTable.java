package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.column.Column;
import fr.awildelephant.rdbms.storage.data.record.MultipleColumnsIterator;
import fr.awildelephant.rdbms.storage.data.record.Record;
import jakarta.annotation.Nonnull;

import java.util.Iterator;
import java.util.List;

public record ColumnBasedTable(Schema schema, List<Column> columns) implements Table {

    @Override
    public int numberOfTuples() {
        return columns.getFirst().size();
    }

    @Override
    public boolean isEmpty() {
        return numberOfTuples() == 0;
    }

    @Override
    @Nonnull
    public Iterator<Record> iterator() {
        return new MultipleColumnsIterator(columns);
    }

    @Override
    public Schema schema() {
        return schema;
    }

    @Override
    public List<Column> columns() {
        return columns;
    }

    @Override
    @Nonnull
    public String toString() {
        return "NewColumnBasedTable[" +
                "schema=" + schema + ", " +
                "columns=" + columns + ']';
    }
}
