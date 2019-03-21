package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.OrderBy.orderBy;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.SortSpecificationList.sortSpecificationList;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.FALSE;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.TRUE;
import static fr.awildelephant.rdbms.ast.value.Interval.interval;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;
import static java.util.Collections.singletonList;

class SelectParserTest {

    @Test
    void it_should_parse_a_select_statement_with_a_single_columns() {
        assertParsing("SELECT y FROM z",

                      select(columns("y"), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_statement_with_several_output_columns() {
        assertParsing("SELECT w, x, y FROM z",

                      select(columns("w", "x", "y"), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_star_statement() {
        assertParsing("SELECT * FROM z",

                      select(singletonList(asterisk()), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_distinct() {
        assertParsing("SELECT DISTINCT a FROM test",

                      distinct(select(columns("a"), tableName("test"))));
    }

    @Test
    void it_should_parse_a_select_with_a_column_alias() {
        assertParsing("SELECT a AS b FROM test",

                      select(List.of(columnAlias(columnName("a"), "b")), tableName("test")));
    }

    @Test
    void it_should_parse_a_parenthesized_column_reference() {
        assertParsing("SELECT (a) FROM test",

                      select(columns("a"), tableName("test")));
    }

    @Test
    void it_should_parse_boolean_constants_in_the_output_columns() {
        assertParsing("SELECT true, false FROM test",

                      select(List.of(TRUE, FALSE), tableName("test")));
    }

    @Test
    void it_should_parse_a_select_query_with_an_order_by_clause() {
        assertParsing("SELECT column1 FROM test ORDER BY column2, column3",

                      select(columns("column1"),
                             orderBy(tableName("test"),
                                     sortSpecificationList(List.of(columnName("column2"), columnName("column3"))))));
    }

    @Test
    void it_should_parse_a_select_query_with_an_interval() {
        assertParsing("VALUES (date '2019-03-21' - INTERVAL '7' DAY (3))",

                      rows(row(minus(cast(textLiteral("2019-03-21"), ColumnDefinition.DATE), interval("7", 3)))));
    }
}
