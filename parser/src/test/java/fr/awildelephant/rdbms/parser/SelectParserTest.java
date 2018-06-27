package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;
import static java.util.Collections.singletonList;

class SelectParserTest {

    @Test
    void it_should_parse_a_select_statement_with_a_single_columns() {
        assertParsing("SELECT y FROM z", select(columns("y"), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_statement_with_several_output_columns() {
        assertParsing("SELECT w, x, y FROM z", select(columns("w", "x", "y"), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_star_statement() {
        assertParsing("SELECT * FROM z", select(singletonList(asterisk()), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_distinct() {
        assertParsing("SELECT DISTINCT a FROM test", select(true, columns("a"), tableName("test")));
    }
}
