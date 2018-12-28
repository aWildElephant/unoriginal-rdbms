package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.ColumnDefinition.DATE;
import static fr.awildelephant.rdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Rows.rows;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.value.DecimalLiteral.decimalLiteral;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.NullLiteral.nullLiteral;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class InsertIntoParserTest {

    @Test
    void it_should_parse_an_insert_into_statement_with_a_single_column_and_a_single_row() {
        assertParsing("INSERT INTO test VALUES (1)", insertInto(tableName("test"), rows(row(List.of(integerLiteral(1))))));
    }

    @Test
    void it_should_parse_an_insert_statement_with_a_single_column_and_several_rows() {
        assertParsing("INSERT INTO test VALUES (1), (2)", insertInto(tableName("test"), rows(row(List.of(integerLiteral(1))), row(List.of(integerLiteral(2))))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_several_columns_and_a_single_row() {
        assertParsing("INSERT INTO test VALUES (1, 2, 3)", insertInto(tableName("test"), rows(row(List.of(integerLiteral(1), integerLiteral(2), integerLiteral(3))))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_a_text_literal() {
        assertParsing("INSERT INTO test VALUES ('some text')", insertInto(tableName("test"), rows(row(List.of(textLiteral("some text"))))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_a_decimal_literal() {
        assertParsing("INSERT INTO test VALUES (19.99)", insertInto(tableName("test"), rows(row(List.of(decimalLiteral(new BigDecimal("19.99")))))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_the_null_value() {
        assertParsing("INSERT INTO test VALUES (null)", insertInto(tableName("test"), rows(row(List.of(nullLiteral())))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_a_date_literal() {
        assertParsing("INSERT INTO test VALUES (date '2018-07-15')", insertInto(tableName("test"), rows(row(List.of(cast(textLiteral("2018-07-15"), DATE))))));
    }
}
