package fr.awildelephant.rdbms.test.commons;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ResultSetAsserter {

    private final ResultSet resultSet;

    private ResultSetAsserter(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public static ResultSetAsserter assertThat(ResultSet resultSet) {
        return new ResultSetAsserter(resultSet);
    }

    // TODO: actual check column types
    public void isExpectedResult(ExpectedResult expected) throws Exception {
        assertColumnNames(resultSet.getMetaData(), expected.columnNames());

        final List<Checker> columnCheckers = expected.columnTypes().stream().map(Checker::checkerFor).toList();

        final List<List<String>> rows = expected.data();

        final int numberOfExpectedRows = rows.size();

        int i = 0;

        while (i < numberOfExpectedRows && resultSet.next()) {
            final List<String> row = rows.get(i);

            for (int j = 0; j < row.size(); j++) {
                columnCheckers.get(j).check(resultSet, i + 1, j + 1, row.get(j));
            }

            i++;
        }

        while (resultSet.next()) {
            i++;
        }

        assertEquals(numberOfExpectedRows, i, "Row count mismatch");
    }

    private void assertColumnNames(ResultSetMetaData metaData, List<String> expectedColumnNames) throws SQLException {
        final int numberOfColumns = metaData.getColumnCount();

        assertEquals(expectedColumnNames.size(), numberOfColumns, "Column count mismatch");

        for (int i = 0; i < numberOfColumns; i++) {
            assertEquals(expectedColumnNames.get(i), metaData.getColumnName(i + 1), "Column name mismatch");
        }
    }
}
