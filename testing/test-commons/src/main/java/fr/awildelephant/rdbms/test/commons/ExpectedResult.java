package fr.awildelephant.rdbms.test.commons;

import java.util.List;

public final class ExpectedResult {

    private final List<String> columnNames;
    private final List<String> columnTypes;
    private final List<List<String>> data;

    public ExpectedResult(List<String> columnNames, List<String> columnTypes, List<List<String>> data) {
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.data = data;
    }

    public List<String> columnNames() {
        return columnNames;
    }

    public List<String> columnTypes() {
        return columnTypes;
    }

    public List<List<String>> data() {
        return data;
    }
}
