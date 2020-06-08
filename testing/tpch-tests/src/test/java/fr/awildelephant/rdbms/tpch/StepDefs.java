package fr.awildelephant.rdbms.tpch;

import fr.awildelephant.csvloader.Loader;
import fr.awildelephant.rdbms.test.commons.ExpectedResult;
import fr.awildelephant.rdbms.test.commons.RDBMSTestWrapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static fr.awildelephant.rdbms.test.commons.ResultSetAsserter.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StepDefs implements En {

    private static final Logger LOGGER = LogManager.getLogger(StepDefs.class);
    private static final String TPCH_DATA_DIRECTORY = "TPCH_DATA_DIRECTORY";
    private static final String SCHEMA_PATH = "/schema/%s.sql";

    private static RDBMSTestWrapper testWrapper;
    private static Set<String> createdTables;
    private static Set<String> loadedTables;

    public StepDefs() throws SQLException {
        if (testWrapper == null) {
            LOGGER.info("Creating test wrapper");
            testWrapper = new RDBMSTestWrapper("tpch");
            loadedTables = new HashSet<>();
        } else {
            LOGGER.info("Reusing existing test wrapper");
        }

        Given("^I load (\\w+) scale factor (\\d+)", (String tableName, Integer scaleFactor) -> {
            final String tpchDataDirectory = System.getenv(TPCH_DATA_DIRECTORY);

            if (tpchDataDirectory == null) {
                throw new MissingEnvironmentVariableException(TPCH_DATA_DIRECTORY);
            }

            if (loadedTables.add(tableName)) {
                testWrapper.execute(createTableQuery(tableName));

                testWrapper.forwardExceptionIfPresent();

                final File compressedCsvDataFile = Paths
                        .get(tpchDataDirectory, String.valueOf(scaleFactor), tableName + ".tbl.gz").toFile();

                new Loader(testWrapper.connection()).load(compressedCsvDataFile, tableName);
            } else {
                LOGGER.info("Table {} is already loaded", tableName);
            }
        });

        When("^I execute the query$", testWrapper::execute);

        Then("^I expect the result$", (DataTable dataTable) -> assertResult(dataTable.asLists()));

        Then("^I expect the result described in (.+)$", fileName -> {
            final InputStream inputStream = this.getClass().getResourceAsStream("/results/" + fileName);

            final CSVParser parser = CSVFormat.DEFAULT.parse(new BufferedReader(new InputStreamReader(inputStream)));

            final List<List<String>> expectedResult = parser.getRecords().stream()
                    .map(this::toList)
                    .collect(Collectors.toList());

            assertResult(expectedResult);
        });
    }

    private List<String> toList(CSVRecord record) {
        final List<String> list = new ArrayList<>(record.size());

        for (String item : record) {
            list.add(item);
        }

        return list;
    }

    private String createTableQuery(String tableName) {
        final InputStream stream = this.getClass()
                                       .getResourceAsStream(String.format(SCHEMA_PATH, tableName));

        if (stream == null) {
            throw new NoSuchElementException("ResultProxy " + tableName + " is not a known TPC-H table");
        }

        try (final Scanner scanner = new Scanner(stream).useDelimiter("\\A")) {
            return scanner.next();
        }
    }

    private void assertResult(List<List<String>> expectedResult) throws Exception {
        testWrapper.forwardExceptionIfPresent();

        final ResultSet lastResult = testWrapper.getStatement().getResultSet();

        assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

        final List<String> expectedColumnNames = expectedResult.get(0);
        final List<String> expectedColumnTypes = expectedResult.get(1);
        final List<List<String>> rows = expectedResult.subList(2, expectedResult.size());

        final ExpectedResult expected = new ExpectedResult(expectedColumnNames, expectedColumnTypes, rows);

        assertThat(lastResult).isExpectedResult(expected);
    }
}
