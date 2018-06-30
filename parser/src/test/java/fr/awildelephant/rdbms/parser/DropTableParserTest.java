package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.DropTable.dropTable;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class DropTableParserTest {

    @Test
    void it_should_parse_a_drop_table_statement() {
        assertParsing("DROP TABLE test", dropTable(tableName("test")));
    }
}
