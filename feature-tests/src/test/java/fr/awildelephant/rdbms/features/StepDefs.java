package fr.awildelephant.rdbms.features;

import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;

public class StepDefs implements En {

    public StepDefs() {

        When("I execute the update", (String query) -> {
        });

        When("I execute the query", (String query) -> {
        });

        Then("I expect the result set", (DataTable table) -> {
        });
    }
}
