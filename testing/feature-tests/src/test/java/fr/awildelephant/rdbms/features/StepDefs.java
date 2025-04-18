package fr.awildelephant.rdbms.features;

import fr.awildelephant.rdbms.test.commons.Checker;
import fr.awildelephant.rdbms.test.commons.ExpectedResult;
import fr.awildelephant.rdbms.test.commons.RDBMSTestWrapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import static fr.awildelephant.rdbms.test.commons.ResultSetAsserter.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StepDefs implements En {

    private final RDBMSTestWrapper testWrapper;

    public StepDefs() throws SQLException {

        testWrapper = new RDBMSTestWrapper("feature-tests");

        Given("^the table (\\w+)$", (String name, DataTable content) -> {
            testWrapper.forwardExceptionIfPresent();

            final List<String> columnNames = content.row(0);
            final List<String> columnDefinitions = content.row(1);
            final List<String> columnTypes = columnDefinitions.stream()
                    .map(definition -> definition.split(" ")[0])
                    .toList();

            final StringBuilder createTableBuilder = new StringBuilder("CREATE TABLE ").append(name).append(" (");
            for (int i = 0; i < columnNames.size(); i++) {
                createTableBuilder.append(columnNames.get(i)).append(" ").append(columnDefinitions.get(i));

                if (i < columnNames.size() - 1) {
                    createTableBuilder.append(", ");
                }
            }
            createTableBuilder.append(");");

            testWrapper.execute(createTableBuilder.toString());

            final List<List<String>> values = content.rows(2).asLists();

            if (!values.isEmpty()) {
                final StringBuilder insertIntoBuilder = new StringBuilder("INSERT INTO ").append(name).append(" VALUES");

                boolean first = true;
                for (List<String> row : values) {
                    if (!first) {
                        insertIntoBuilder.append(',');
                    } else {
                        first = false;
                    }

                    insertIntoBuilder.append(" (");

                    for (int i = 0; i < columnNames.size(); i++) {
                        final String columnType = columnTypes.get(i);

                        final String value = row.get(i);

                        if (value.equalsIgnoreCase("null")) {
                            insertIntoBuilder.append(value);
                        } else {
                            if (columnType.equalsIgnoreCase("DATE")) {
                                insertIntoBuilder.append("date ");
                            }

                            if (columnType.equalsIgnoreCase("TEXT") || columnType.equalsIgnoreCase("DATE")) {
                                insertIntoBuilder.append('\'');
                            }

                            insertIntoBuilder.append(value);

                            if (columnType.equalsIgnoreCase("TEXT") || columnType.equalsIgnoreCase("DATE")) {
                                insertIntoBuilder.append('\'');
                            }
                        }

                        if (i < columnNames.size() - 1) {
                            insertIntoBuilder.append(", ");
                        }
                    }

                    insertIntoBuilder.append(")");
                }

                insertIntoBuilder.append(';');

                testWrapper.execute(insertIntoBuilder.toString());
            }
        });

        When("I execute the query", testWrapper::execute);

        Then("I expect the result set", (DataTable content) -> assertResult(content));

        Then("I expect a result set with no column and {int} rows", (Integer numberOfRows) -> {
            // Add two rows for where the name/type should have been
            final List<List<String>> expectedData = IntStream.range(0, numberOfRows + 2).mapToObj(unused -> List.<String>of()).toList();

            assertResult(expectedData);
        });

        Then("^table (\\w+) should be$", (String name, DataTable content) -> {
            testWrapper.execute("SELECT * FROM " + name);

            assertResult(content);
        });

        Then("^there is no table named (\\w+)$", (String name) -> {
            testWrapper.execute("SELECT * FROM " + name);

            assertException("Table not found: " + name);
        });

        Then("^I expect an error with the message$", this::assertException);

        Then("^I expect the plan$", (String expectedPlan) -> {
            testWrapper.forwardExceptionIfPresent();

            final ResultSet lastResult = testWrapper.lastResultSet();

            assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

            // TODO: assert number of rows/columns

            lastResult.next();

            final String plan = lastResult.getString(1);

            assertEquals(removeBlankCharacters(expectedPlan), removeBlankCharacters(plan));
        });
    }

    private String removeBlankCharacters(String text) {
        return text.replaceAll("\\s", "");
    }

    private void assertException(String expectedErrorMessage) {
        final SQLException lastException = testWrapper.lastException();

        assertNotNull(lastException, "No exception was thrown");

        final String actualErrorMessage = lastException.getMessage();

        assertEquals(expectedErrorMessage, actualErrorMessage, "Expected an error message");
    }

    private void assertResult(DataTable expected) throws Exception {
        assertResult(expected.asLists());
    }

    private void assertResult(List<List<String>> expected) throws Exception {
        testWrapper.forwardExceptionIfPresent();

        final ResultSet lastResult = testWrapper.lastResultSet();

        assertNotNull(lastResult, "Result set is null: no query run or last query was an update");

        final List<String> expectedColumnNames = expected.getFirst();
        final List<Checker> expectedColumnTypes = expected.get(1).stream().map(Checker::checkerFor).toList();
        final List<List<String>> rows = expected.subList(2, expected.size());

        final ExpectedResult expectedResult = new ExpectedResult(expectedColumnNames, expectedColumnTypes, rows);

        assertThat(lastResult).isExpectedResult(expectedResult);
    }
}
