package fr.awildelephant.rdbms.test.commons;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ResultSetAsserter {

    private final ResultSet resultSet;

    private ResultSetAsserter(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public static ResultSetAsserter assertThat(ResultSet resultSet) {
        return new ResultSetAsserter(resultSet);
    }

    public void isExpectedResult(ExpectedResult expected) throws Exception {
        checkResultSetMetadata(expected);

        final List<Checker> columnCheckers = expected.columnTypes();

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

    private void checkResultSetMetadata(ExpectedResult expected) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();

        final int numberOfColumns = metaData.getColumnCount();
        final List<String> expectedColumnNames = expected.columnNames();
        final List<Checker> expectedColumnTypes = expected.columnTypes();

        assertEquals(expectedColumnNames.size(), numberOfColumns, "Column count mismatch");

        for (int i = 0; i < numberOfColumns; i++) {
            assertEquals(expectedColumnNames.get(i), metaData.getColumnName(i + 1), "Column name mismatch");
            assertTrue(expectedColumnTypes.get(i).supports(metaData.getColumnType(i + 1)), "Column type " + metaData.getColumnTypeName(i + 1) + " not supported by checker " + expectedColumnTypes.get(i).name());
        }
    }
}
