package fr.awildelephant.rdbms.embedded;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.Value;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public class TableProxy implements ResultProxy {

    private final List<Record> rows;
    private final Schema schema;

    private final ValueProxy valueProxy = new ValueProxy();

    TableProxy(Table table) {
        final List<Record> rows = new ArrayList<>(table.numberOfTuples());

        for (Record record : table) {
            rows.add(record);
        }

        this.rows = rows;
        this.schema = table.schema();
    }

    @Override
    public Value get(int row, int column) {
        valueProxy.setValue(rows.get(row).get(column));

        return valueProxy;
    }

    @Override
    public int position(String columnName) {
        final Column column = schema.column(columnName);

        if (column == null) {
            return -1;
        }

        return column.index();
    }

    @Override
    public String columnName(int column) {
        return schema.columnNames().get(column);
    }

    @Override
    public String columnTypeName(int column) {
        return schema.column(columnName(column)).domain().name();
    }

    @Override
    public boolean isNullable(int column) {
        return !schema.column(columnName(column)).notNull();
    }

    @Override
    public int numberOfRows() {
        return rows.size();
    }

    @Override
    public int numberOfColumns() {
        return schema.numberOfAttributes();
    }
}
