package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.FunctionCall.functionCall;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class FunctionCallParserTest {

    @Test
    void it_should_parse_a_function_in_from_clause() {
        assertParsing("SELECT * FROM csv('/home/etienne/test.csv')",

                select(List.of(asterisk()), functionCall("csv", List.of(textLiteral("/home/etienne/test.csv"))), null, null, null, null));
    }

    @Disabled // TODO
    @Test
    void it_should_parse_a_function_in_the_output_columns_of_a_select_query() {
        assertParsing("SELECT complex_computation(0, 0) FROM la_tete_a_toto",

                select(List.of(functionCall("complex_computation", List.of(integerLiteral(0), integerLiteral(0)))), tableName("la_tete_a_toto"), null, null, null, null));
    }
}
