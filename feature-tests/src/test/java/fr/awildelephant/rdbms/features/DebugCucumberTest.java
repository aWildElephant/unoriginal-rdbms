package fr.awildelephant.rdbms.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore
@CucumberOptions(glue = {"fr.awildelephant.rdbms.features"}, tags = "@debug")
@RunWith(Cucumber.class)
@Ignore // TODO: this test could be use to make the build fail if a feature test marked as todo passes
public class DebugCucumberTest {
}
