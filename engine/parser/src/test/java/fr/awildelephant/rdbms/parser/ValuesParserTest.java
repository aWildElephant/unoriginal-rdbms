package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

public class ValuesParserTest {

    @Test
    void it_should_parse_a_values_statement_with_no_row() {
        assertParsing("VALUES", rows());
    }

    @Test
    void it_should_parse_a_values_statement_with_no_column() {
        assertParsing("VALUES (), ()",

                rows(row(), row()));
    }
}
