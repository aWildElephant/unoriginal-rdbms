package fr.awildelephant.rdbms.features;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("fr.awildelephant.rdbms.features")
@ExcludeTags("todo")
public class CucumberTest {
}
