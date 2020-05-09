package fr.awildelephant.rdbms.features;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@CucumberOptions(glue = {"fr.awildelephant.rdbms.features"}, tags = "not @todo")
@RunWith(Cucumber.class)
public class CucumberTest {
}
