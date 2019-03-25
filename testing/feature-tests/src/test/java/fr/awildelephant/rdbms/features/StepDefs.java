package fr.awildelephant.rdbms.features;

import cucumber.api.java8.En;
import fr.awildelephant.rdbms.cucumber.step.definitions.Checker;
import fr.awildelephant.rdbms.cucumber.step.definitions.RDBMSTestWrapper;
import io.cucumber.datatable.DataTable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefs implements En {

    private final RDBMSTestWrapper testWrapper;

    public StepDefs() throws SQLException {

        testWrapper = new RDBMSTestWrapper("feature-tests");

        Given("^the table (\\w+)$", (String name, DataTable content) -> {
            testWrapper.forwardExceptionIfPresent();

            final List<String> columnNames = content.row(0);
            final List<String> columnDefinitions = content.row(1);
            final List<String> columnTypes = columnDefinitions.stream().map(definition -> definition.split(" ")[0])
                                                              .collect(toList());

            final StringBuilder createTableBuilder = new StringBuilder("CREATE TABLE ").append(name).append(" (");
            for (int i = 0; i < columnNames.size(); i++) {
                createTableBuilder.append(columnNames.get(i)).append(" ").append(columnDefinitions.get(i));

                if (i < columnNames.size() - 1) {
                    createTableBuilder.append(", ");
                }
            }
            createTableBuilder.append(");");

            testWrapper.execute(createTableBuilder.toString());

            for (List<String> row : content.rows(2).asLists()) {
                final StringBuilder insertIntoBuilder = new StringBuilder("INSERT INTO ").append(name)
                                                                                         .append(" VALUES (");

                for (int i = 0; i < columnNames.size(); i++) {
                    final String columnType = columnTypes.get(i);

                    if (columnType.equalsIgnoreCase("TEXT")) {
                        insertIntoBuilder.append('\'');
                    }

                    insertIntoBuilder.append(row.get(i));

                    if (columnType.equalsIgnoreCase("TEXT")) {
                        insertIntoBuilder.append('\'');
                    }

                    if (i < columnNames.size() - 1) {
                        insertIntoBuilder.append(", ");
                    }
                }

                insertIntoBuilder.append(");");

                testWrapper.execute(insertIntoBuilder.toString());
            }
        });

        When("I execute the query", testWrapper::execute);

        Then("I expect the result set", this::assertResult);

        Then("^table (\\w+) should be$", (String name, DataTable content) -> {
            testWrapper.execute("SELECT * FROM " + name);

            assertResult(content);
        });

        Then("^there is no table named (\\w+)$", (String name) -> {
            testWrapper.execute("SELECT * FROM " + name);

            assertException("Table not found: " + name);
        });

        Then("^I expect an error with the message$", this::assertException);
    }

    private void assertException(String expectedErrorMessage) {
        final SQLException lastException = testWrapper.lastException();

        assertNotNull(lastException, "No exception was thrown");

        final String actualErrorMessage = lastException.getMessage();

        assertEquals(expectedErrorMessage, actualErrorMessage, "Expected an error message");
    }

    private void assertResult(DataTable table) throws Exception {
        testWrapper.forwardExceptionIfPresent();

        final ResultSet lastResult = testWrapper.getStatement().getResultSet();

        assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

        final List<List<String>> expectedResult = table.asLists();

        final List<String> expectedColumnNames = expectedResult.get(0);
        final List<String> expectedColumnTypes = expectedResult.get(1);

        assertColumnNames(lastResult.getMetaData(), expectedColumnNames);

        final List<Checker> columnCheckers = expectedColumnTypes.stream()
                                                                .map(Checker::checkerFor)
                                                                .collect(toList());
        final List<List<String>> rows = expectedResult.subList(2, expectedResult.size());

        final int numberOfExpectedRows = rows.size();

        int i = 0;

        while (i < numberOfExpectedRows) {
            assertTrue(lastResult.next(), "Expected " + numberOfExpectedRows + " rows but got " + i);

            final List<String> row = rows.get(i);

            for (int j = 0; j < row.size(); j++) {
                columnCheckers.get(j).check(lastResult, i + 1, j + 1, row.get(j));
            }

            i++;
        }

        while (lastResult.next()) {
            i++;
        }

        assertEquals(numberOfExpectedRows, i, "Expected " + numberOfExpectedRows + " rows but got " + i);
    }

    private void assertColumnNames(ResultSetMetaData metaData, List<String> expectedColumnNames) throws SQLException {
        final int numberOfColumns = metaData.getColumnCount();

        assertEquals(expectedColumnNames.size(), numberOfColumns, "Number of columns");

        for (int i = 0; i < numberOfColumns; i++) {
            assertEquals(expectedColumnNames.get(i), metaData.getColumnName(i + 1), "Column name mismatch");
        }
    }
}
