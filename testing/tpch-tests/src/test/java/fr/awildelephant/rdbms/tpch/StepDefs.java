package fr.awildelephant.rdbms.tpch;

import cucumber.api.java8.En;
import fr.awildelephant.csvloader.Loader;
import fr.awildelephant.rdbms.test.commons.ExpectedResult;
import fr.awildelephant.rdbms.test.commons.RDBMSTestWrapper;
import io.cucumber.datatable.DataTable;

import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static fr.awildelephant.rdbms.test.commons.ResultSetAsserter.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StepDefs implements En {

    private static final String SCHEMA_PATH = "/schema/%s.sql";

    private final RDBMSTestWrapper testWrapper;

    public StepDefs() throws SQLException {

        testWrapper = new RDBMSTestWrapper("feature-tests");

        Given("^I create the TPC-H (\\w+) table$", (String tableName) -> {
            testWrapper.execute(createTableQuery(tableName));

            testWrapper.forwardExceptionIfPresent();
        });

        Given("^I load (\\w+) scale factor (\\d+) data", (String tableName, Integer scaleFactor) -> {
            // TODO: absolute path lol
            // TODO; proper error handling if the table/the scale factor is not found
            final File compressedCsvDataFile = new File(
                    "/home/etienne/Code/rdbms/testing/tpch-tests/data/" + scaleFactor + "/" + tableName + ".tbl.gz");

            new Loader(testWrapper.connection()).load(compressedCsvDataFile, tableName);
        });

        When("^I execute the query$", testWrapper::execute);

        Then("^I expect the result$", this::assertResult);
    }

    private String createTableQuery(String tableName) {
        final InputStream stream = this.getClass()
                                       .getResourceAsStream(String.format(SCHEMA_PATH, tableName));

        if (stream == null) {
            throw new NoSuchElementException("Table " + tableName + " is not a known TPC-H table");
        }

        try (final Scanner scanner = new Scanner(stream).useDelimiter("\\A")) {
            return scanner.next();
        }
    }

    private void assertResult(DataTable table) throws Exception {
        testWrapper.forwardExceptionIfPresent();

        final ResultSet lastResult = testWrapper.getStatement().getResultSet();

        assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

        final List<List<String>> expectedResult = table.asLists();

        final List<String> expectedColumnNames = expectedResult.get(0);
        final List<String> expectedColumnTypes = expectedResult.get(1);
        final List<List<String>> rows = expectedResult.subList(2, expectedResult.size());

        final ExpectedResult expected = new ExpectedResult(expectedColumnNames, expectedColumnTypes, rows);

        assertThat(lastResult).isExpectedResult(expected);
    }
}
