package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.Function.function;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class FunctionParserTest {

    @Test
    void it_should_parse_a_function_in_from_clause() {
        assertParsing("SELECT * FROM csv('/home/etienne/test.csv')",

                select(List.of(asterisk()), function("csv", List.of(textLiteral("/home/etienne/test.csv"))), null, null, null, null));
    }
}
