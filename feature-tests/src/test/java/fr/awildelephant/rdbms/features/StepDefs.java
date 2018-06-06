package fr.awildelephant.rdbms.features;

import cucumber.api.java8.En;
import fr.awildelephant.rdbms.client.RDBMSDriver;
import io.cucumber.datatable.DataTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

            throw new UnsupportedOperationException();
        });
    }
}
