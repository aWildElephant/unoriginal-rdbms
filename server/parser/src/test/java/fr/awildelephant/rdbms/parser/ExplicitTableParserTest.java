package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class ExplicitTableParserTest {

    @Test
    void it_should_parse_an_explicit_table() {
        assertParsing("TABLE test", tableName("test"));
    }
}
