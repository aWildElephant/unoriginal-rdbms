package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.MultipleColumnsIterator;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.schema.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public record NewColumnBasedTable(Schema schema, List<Column> columns) implements Table {

    @Override
    public int numberOfTuples() {
        return columns.get(0).size();
    }

    @Override
    public boolean isEmpty() {
        return numberOfTuples() == 0;
    }

    @Override
    @NotNull
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (NewColumnBasedTable) obj;
        return Objects.equals(this.schema, that.schema) &&
                Objects.equals(this.columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, columns);
    }

    @Override
    public String toString() {
        return "NewColumnBasedTable[" +
                "schema=" + schema + ", " +
                "columns=" + columns + ']';
    }
}
