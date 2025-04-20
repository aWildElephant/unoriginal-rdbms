package fr.awildelephant.rdbms.test.commons;

import fr.awildelephant.rdbms.util.structure.matrix.Matrix;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        final List<ExpectedColumn> expectedColumns = expected.columns();
        final int expectedNumberOfColumns = expectedColumns.size();
        final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        final int actualNumberOfColumns = resultSetMetaData.getColumnCount();
        assertEquals(expectedNumberOfColumns, actualNumberOfColumns, "Column count mismatch: expected " + expectedNumberOfColumns + " but got " + actualNumberOfColumns);

        for (int i = 0; i < expectedNumberOfColumns; i++) {
            final ExpectedColumn expectedColumn = expectedColumns.get(i);

            final String expectedColumnName = expectedColumn.name();
            final String actualColumnName = resultSetMetaData.getColumnName(i + 1);
            assertEquals(expectedColumnName, actualColumnName, "Column name mismatch: expected " + expectedColumnName + " but got " + actualColumnName);

            final Checker expectedColumnType = expectedColumn.checker();
            final int actualColumnType = resultSetMetaData.getColumnType(i + 1);
            assertTrue(expectedColumnType.supports(actualColumnType), "Column type " + resultSetMetaData.getColumnTypeName(i + 1) + " not supported by checker " + expectedColumnType.name());
        }

        final Matrix<String> expectedContent = expected.content();
        final int numberOfExpectedRows = expectedContent.numberOfRows();

        int i = 0;

        while (i < numberOfExpectedRows && resultSet.next()) {
            for (int j = 0; j < expectedNumberOfColumns; j++) {
                final ExpectedColumn expectedColumn = expectedColumns.get(j);
                expectedColumn.checker().check(resultSet, i + 1, j + 1, expectedContent.get(j, i));
            }

            i++;
        }

        while (resultSet.next()) {
            i++;
        }

        assertEquals(numberOfExpectedRows, i, "Row count mismatch");
    }
}
