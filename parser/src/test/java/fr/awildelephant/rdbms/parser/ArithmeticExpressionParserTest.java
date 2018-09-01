package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.ast.Select.select;
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

                      select(List.of(plus(columnName("a"), integerLiteral(1))), tableName("test")));
    }

    @Test
    void it_should_parse_an_subtraction() {
        assertParsing("SELECT a - 1 FROM test",

                      select(List.of(minus(columnName("a"), integerLiteral(1))), tableName("test")));
    }

    @Test
    void it_should_parse_a_multiplication() {
        assertParsing("SELECT a * 42 FROM test",

                      select(List.of(multiply(columnName("a"), integerLiteral(42))), tableName("test")));
    }

    @Test
    void it_should_parse_a_division() {
        assertParsing("SELECT a / 2.0 FROM test",

                      select(List.of(divide(columnName("a"), decimalLiteral(BigDecimal.valueOf(2.0)))), tableName("test")));
    }
}
