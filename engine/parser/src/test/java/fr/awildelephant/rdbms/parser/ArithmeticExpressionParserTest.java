package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.TableName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
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

                select()
                        .outputColumns(plus(unqualifiedColumnName("a"), integerLiteral(1)))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_an_subtraction() {
        assertParsing("SELECT a - 1 FROM test",

                select()
                        .outputColumns(minus(unqualifiedColumnName("a"), integerLiteral(1)))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_a_multiplication() {
        assertParsing("SELECT a * 42 FROM test",

                select()
                        .outputColumns(multiply(unqualifiedColumnName("a"), integerLiteral(42)))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_a_division() {
        assertParsing("SELECT a / 2. FROM test",

                select()
                        .outputColumns(divide(unqualifiedColumnName("a"), decimalLiteral(BigDecimal.valueOf(2))))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_an_addition_followed_by_a_multiplication() {
        assertParsing("SELECT a + b * c FROM test",

                select()
                        .outputColumns(plus(unqualifiedColumnName("a"), multiply(unqualifiedColumnName("b"),
                                unqualifiedColumnName("c"))))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_a_multiplication_and_an_addition_with_the_correct_precedence() {
        assertParsing("SELECT a * b + c FROM test",

                select()
                        .outputColumns(plus(multiply(unqualifiedColumnName("a"), unqualifiedColumnName("b")),
                                unqualifiedColumnName("c")))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_the_addition_of_operations_with_more_precedence() {
        assertParsing("SELECT a * 2 + b / 3 FROM test",

                select()
                        .outputColumns(plus(multiply(unqualifiedColumnName("a"), integerLiteral(2)), divide(
                                unqualifiedColumnName("b"), integerLiteral(3))))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_parenthesis_in_a_numeric_expression() {
        assertParsing("SELECT 2 * (a + b) FROM test",

                select()
                        .outputColumns(multiply(integerLiteral(2), plus(unqualifiedColumnName("a"),
                                unqualifiedColumnName("b"))))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_a_negative_integer_value() {
        assertParsing("SELECT -1000 FROM test",

                select()
                        .outputColumns(integerLiteral(-1000))
                        .fromClause(TableName.tableName("test")));
    }
}
