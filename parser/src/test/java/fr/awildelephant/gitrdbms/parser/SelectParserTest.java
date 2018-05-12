package fr.awildelephant.gitrdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.gitrdbms.ast.Select.select;
import static fr.awildelephant.gitrdbms.ast.TableName.tableName;
import static fr.awildelephant.gitrdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.gitrdbms.parser.ParserTestHelper.columns;

class SelectParserTest {

    @Test
    void it_should_parse_a_select_statement_with_a_single_columns() {
        assertParsing("SELECT y FROM z", select(columns("y"), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_statement_with_several_output_columns() {
        assertParsing("SELECT w, x, y FROM z", select(columns("w", "x", "y"), tableName("z")));
    }
}
