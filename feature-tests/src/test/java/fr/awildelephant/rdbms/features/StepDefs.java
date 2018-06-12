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
import static org.junit.jupiter.api.Assertions.*;

public class StepDefs implements En {

    private final Connection connection;
    private Statement lastStatement;

    public StepDefs() throws SQLException {

        connection = new RDBMSDriver().connect("jdbc:rdbms:mem:feature-tests", null);

        When("I execute the query", (String query) -> {
            final Statement statement = connection.createStatement();

            statement.execute(query);

            lastStatement = statement;
        });

        Then("I expect the result set", (DataTable table) -> {
            assertNotNull(lastStatement, "Last statement is null: no query run");

            final ResultSet lastResult = lastStatement.getResultSet();

            assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

            final List<List<String>> expectedResult = table.asLists();
            final List<List<String>> rows = expectedResult.subList(2, expectedResult.size());

            final int numberOfExpectedRows = rows.size();

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
    }
}
