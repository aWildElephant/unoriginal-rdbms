package fr.awildelephant.rdbms.util.printer.table;

import java.util.List;

public class Table {

    private final List<Row> headerRows;
    private final List<Row> rows;

    public Table(List<Row> headerRows, List<Row> rows) {
        this.headerRows = headerRows;
        this.rows = rows;
    }

    public List<Row> headerRows() {
        return headerRows;
    }

    public List<Row> rows() {
        return rows;
    }
}
