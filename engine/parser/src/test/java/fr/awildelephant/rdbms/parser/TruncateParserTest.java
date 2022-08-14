package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.Truncate.truncate;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

public class TruncateParserTest {

    @Test
    void it_should_parse_a_truncate_statement() {
        assertParsing("TRUNCATE test", truncate(tableName("test")));
    }
}
