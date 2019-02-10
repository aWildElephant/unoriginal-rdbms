package fr.awildelephant.rdbms.features;

import cucumber.api.java8.En;
import fr.awildelephant.rdbms.client.RDBMSDriver;
import io.cucumber.datatable.DataTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefs implements En {

    private final Connection connection;
    private Statement lastStatement;
    private SQLException lastException;

    public StepDefs() throws SQLException {

        connection = new RDBMSDriver().connect("jdbc:rdbms:mem:feature-tests", null);

        Given("^the table (\\w+)$", (String name, DataTable content) -> {
            forwardExceptionIfPresent();

            final List<String> columnNames = content.row(0);
            final List<String> columnDefinitions = content.row(1);
            final List<String> columnTypes = columnDefinitions.stream().map(definition -> definition.split(" ")[0]).collect(toList());

            final StringBuilder createTableBuilder = new StringBuilder("CREATE TABLE ").append(name).append(" (");
            for (int i = 0; i < columnNames.size(); i++) {
                createTableBuilder.append(columnNames.get(i)).append(" ").append(columnDefinitions.get(i));

                if (i < columnNames.size() - 1) {
                    createTableBuilder.append(", ");
                }
            }
            createTableBuilder.append(");");

            execute(createTableBuilder.toString());

            for (List<String> row : content.rows(2).asLists()) {
                final StringBuilder insertIntoBuilder = new StringBuilder("INSERT INTO ").append(name).append(" VALUES (");

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

                execute(insertIntoBuilder.toString());
            }
        });

        When("I execute the query", this::execute);

        Then("I expect the result set", this::assertResult);

        Then("^table (\\w+) should be$", (String name, DataTable content) -> {
            execute("SELECT * FROM " + name);

            assertResult(content);
        });

        Then("^there is no table named (\\w+)$", (String name) -> {
            execute("SELECT * FROM " + name);

            assertException("Table not found: " + name);
        });

        Then("^I expect an error with the message$", this::assertException);
    }

    private void assertException(String expectedErrorMessage) {
        assertNotNull(lastException, "No exception was thrown");

        final String actualErrorMessage = lastException.getMessage();

        assertEquals(expectedErrorMessage, actualErrorMessage, "Expected an error with the message \"" + expectedErrorMessage + "\" but got \"" + actualErrorMessage + "\"");
    }

    // TODO: check that the column names are what we expect
    private void assertResult(DataTable table) throws Exception {
        forwardExceptionIfPresent();

        final ResultSet lastResult = lastStatement.getResultSet();

        assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

        final List<List<String>> expectedResult = table.asLists();
        final List<Checker> columnCheckers = expectedResult.get(1).stream()
                                                           .map(Checker::checkerFor)
                                                           .collect(toList());
        final List<List<String>> rows = expectedResult.subList(2, expectedResult.size());

        final int numberOfExpectedRows = rows.size();

        // TODO: check that the actual number of column is the same as the expected

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

    private void forwardExceptionIfPresent() throws SQLException {
        if (lastException != null) {
            throw lastException;
        }
    }

    private void execute(String query) throws SQLException {
        forwardExceptionIfPresent();

        try {
            reset();

            final Statement statement = connection.createStatement();

            statement.execute(query);

            lastStatement = statement;
        } catch (SQLException e) {
            lastException = e;
        }
    }

    private void reset() throws SQLException {
        if (lastStatement != null) {
            lastStatement.close();
            lastStatement = null;
        }
    }
}
