package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static fr.awildelephant.rdbms.ast.IdentifierChain.identifierChain;
import static fr.awildelephant.rdbms.ast.SortedSelect.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.value.DecimalLiteral.decimalLiteral;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class ArithmeticExpressionParserTest {

    @Test
    void it_should_parse_an_addition() {
        assertParsing("SELECT a + 1 FROM test",

                      select(List.of(plus(identifierChain("a"), integerLiteral(1))), tableName("test")));
    }

    @Test
    void it_should_parse_an_subtraction() {
        assertParsing("SELECT a - 1 FROM test",

                      select(List.of(minus(identifierChain("a"), integerLiteral(1))), tableName("test")));
    }

    @Test
    void it_should_parse_a_multiplication() {
        assertParsing("SELECT a * 42 FROM test",

                      select(List.of(multiply(identifierChain("a"), integerLiteral(42))), tableName("test")));
    }

    @Test
    void it_should_parse_a_division() {
        assertParsing("SELECT a / 2. FROM test",

                      select(List.of(divide(identifierChain("a"), decimalLiteral(BigDecimal.valueOf(2)))),
                             tableName("test")));
    }

    @Test
    void it_should_parse_an_addition_followed_by_a_multiplication() {
        assertParsing("SELECT a + b * c FROM test",

                      select(List.of(plus(identifierChain("a"), multiply(identifierChain("b"), identifierChain("c")))),
                             tableName("test")));
    }

    @Test
    void it_should_parse_a_multiplication_and_an_addition_with_the_correct_precedence() {
        assertParsing("SELECT a * b + c FROM test",

                      select(List.of(plus(multiply(identifierChain("a"), identifierChain("b")), identifierChain("c"))),
                             tableName("test")));
    }

    @Test
    void it_should_parse_the_addition_of_operations_with_more_precedence() {
        assertParsing("SELECT a * 2 + b / 3 FROM test",

                      select(List.of(plus(multiply(identifierChain("a"), integerLiteral(2)), divide(
                              identifierChain("b"), integerLiteral(3)))), tableName("test")));
    }

    @Test
    void it_should_parse_parenthesis_in_a_numeric_expression() {
        assertParsing("SELECT 2 * (a + b) FROM test",

                      select(List.of(multiply(integerLiteral(2), plus(identifierChain("a"), identifierChain("b")))),
                             tableName("test")));
    }

    @Test
    void it_should_parse_a_negative_integer_value() {
        assertParsing("SELECT -1000 FROM test",

                      select(List.of(integerLiteral(-1000)), tableName("test")));
    }
}
