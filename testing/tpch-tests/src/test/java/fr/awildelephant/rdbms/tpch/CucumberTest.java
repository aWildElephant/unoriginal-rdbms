package fr.awildelephant.rdbms.tpch;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@CucumberOptions(glue = {"fr.awildelephant.rdbms.tpch"}, tags = "not @todo")
@RunWith(Cucumber.class)
public class CucumberTest {
}
