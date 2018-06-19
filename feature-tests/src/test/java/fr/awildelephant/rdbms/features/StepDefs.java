package fr.awildelephant.rdbms.features;

import cucumber.api.java8.En;
import fr.awildelephant.rdbms.client.RDBMSDriver;
import io.cucumber.datatable.DataTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.*;

public class StepDefs implements En {

    private final Connection connection;
    private Statement lastStatement;
    private SQLException lastException;

    public StepDefs() throws SQLException {

        connection = new RDBMSDriver().connect("jdbc:rdbms:mem:feature-tests", null);

        When("I execute the query", this::execute);

        Then("I expect the result set", (DataTable table) -> {
            assertNotNull(lastStatement, "Last statement is null: no query run");

            final ResultSet lastResult = lastStatement.getResultSet();

            assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

            final List<List<String>> expectedResult = table.asLists();
            final List<List<String>> rows = expectedResult.subList(2, expectedResult.size());

            final int numberOfExpectedRows = rows.size();

            // TODO: check that the actual number of column is the same as the expected

            int i = 0;

            while (i < numberOfExpectedRows) {
                assertTrue(lastResult.next(), "Expected " + numberOfExpectedRows + " rows but got " + i);

                final List<String> row = rows.get(i);

                for (int j = 0; j < row.size(); j++) {
                    final int expected = parseInt(row.get(j));
                    final int actual = lastResult.getInt(j + 1);

                    assertEquals(expected, actual, "Row " + (i + 1) + " column " + (j + 1) + " : expected " + expected + " but got " + actual);
                }

                i++;
            }

            while (lastResult.next()) {
                i++;
            }

            assertEquals(numberOfExpectedRows, i, "Expected " + numberOfExpectedRows + " rows but got " + i);
        });

        Given("^the table (\\w+)$", (String name, DataTable definition) -> {
            final List<String> columnNames = definition.row(0);

            // definition.row(1) is ignored for now since we only support integer type

            execute("CREATE TABLE " +
                    name +
                    " (" +
                    columnNames.stream().map(columnName -> columnName + " INTEGER").collect(joining(", ")) +
                    ");");

            definition.rows(2).asLists().forEach(row -> execute("INSERT INTO " +
                    name +
                    " VALUES (" +
                    row.stream().collect(joining(", ")) +
                    ");"));
        });

        Then("^I expect an error with the message$", (String expectedErrorMessage) -> {
            assertNotNull(lastException, "No exception was thrown");

            final String actualErrorMessage = lastException.getMessage();

            assertEquals(expectedErrorMessage, actualErrorMessage, "Expected an error with the message \"" + expectedErrorMessage + "\" but got \"" + actualErrorMessage + "\"");
        });
    }

    private void execute(String query) {
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
        lastException = null;

        if (lastStatement != null) {
            lastStatement.close();
            lastStatement = null;
        }
    }
}
