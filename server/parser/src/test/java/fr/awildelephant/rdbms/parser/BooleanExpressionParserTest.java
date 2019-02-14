package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.FALSE;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.TRUE;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class BooleanExpressionParserTest {

    @Test
    void it_should_parse_an_and_expression() {
        assertParsing("VALUES (TRUE AND TRUE)", rows(row(and(TRUE, TRUE))));
    }

    @Test
    void it_should_parse_an_or_expression() {
        assertParsing("VALUES (FALSE OR TRUE)", rows(row(or(FALSE, TRUE))));
    }

    @Test
    void it_should_parse_a_not_expression() {
        assertParsing("VALUES (NOT TRUE)", rows(row(not(TRUE))));
    }
}
