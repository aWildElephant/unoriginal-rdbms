package fr.awildelephant.rdbms.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@CucumberOptions(glue = {"fr.awildelephant.rdbms.features"})
@RunWith(Cucumber.class)
public class CucumberTest {
}
