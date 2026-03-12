package fr.awildelephant.rdbms.tpch;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("fr.awildelephant.rdbms.tpch")
public class CucumberTest {
}
