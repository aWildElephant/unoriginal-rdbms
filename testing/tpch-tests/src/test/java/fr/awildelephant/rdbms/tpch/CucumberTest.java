package fr.awildelephant.rdbms.tpch;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@CucumberOptions(glue = {"fr.awildelephant.rdbms.tpch"}, tags = "not @todo")
@RunWith(Cucumber.class)
public class CucumberTest {
}
