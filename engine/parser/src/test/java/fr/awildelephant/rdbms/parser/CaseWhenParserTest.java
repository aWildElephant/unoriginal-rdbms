package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.CaseWhen.caseWhen;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class CaseWhenParserTest {

    @Test
    void it_should_parse_a_case_when_expression() {
        assertParsing("VALUES (CASE WHEN true THEN 1 ELSE 0 END)",

                      rows(row(caseWhen(BooleanLiteral.TRUE, integerLiteral(1), integerLiteral(0)))));
    }
}
