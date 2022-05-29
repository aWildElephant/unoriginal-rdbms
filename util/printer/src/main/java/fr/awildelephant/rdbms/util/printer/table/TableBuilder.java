package fr.awildelephant.rdbms.util.printer.table;

import java.util.ArrayList;
import java.util.List;

public class TableBuilder {

    private final List<Row> headerRows = new ArrayList<>();
    private final List<Row> rows = new ArrayList<>();

    public static TableBuilder builder() {
        return new TableBuilder();
    }

    public TableBuilder addHeaderRow(String... row) {
        return addHeaderRow(List.of(row));
    }

    public TableBuilder addHeaderRow(List<String> row) {
        return addHeaderRow(new Row(row));
    }

    public TableBuilder addHeaderRow(Row row) {
        headerRows.add(row);

        return this;
    }

    public TableBuilder addRow(String... row) {
        return addRow(List.of(row));
    }

    public TableBuilder addRow(List<String> row) {
        return addRow(new Row(row));
    }

    public TableBuilder addRow(Row row) {
        rows.add(row);

        return this;
    }

    public Table build() {
        return new Table(headerRows, rows);
    }
}
