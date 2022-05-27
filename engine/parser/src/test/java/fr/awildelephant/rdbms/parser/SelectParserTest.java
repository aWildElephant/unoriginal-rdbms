package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.InnerJoin.innerJoin;
import static fr.awildelephant.rdbms.ast.Limit.limit;
import static fr.awildelephant.rdbms.ast.OrderingSpecification.ASCENDING;
import static fr.awildelephant.rdbms.ast.OrderingSpecification.DESCENDING;
import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.SortSpecification.sortSpecification;
import static fr.awildelephant.rdbms.ast.SortSpecificationList.sortSpecificationList;
import static fr.awildelephant.rdbms.ast.Substring.substring;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.FALSE;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.TRUE;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;

class SelectParserTest {

    @Test
    void it_should_parse_a_select_statement_with_a_single_columns() {
        assertParsing("SELECT y FROM z",

                select(columns("y"), tableName("z"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_select_statement_with_several_output_columns() {
        assertParsing("SELECT w, x, y FROM z",

                select(columns("w", "x", "y"), tableName("z"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_select_star_statement() {
        assertParsing("SELECT * FROM z",

                select(List.of(asterisk()), tableName("z"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_select_distinct() {
        assertParsing("SELECT DISTINCT a FROM test",

                distinct(select(columns("a"), tableName("test"), null, null, null, null)));
    }

    @Test
    void it_should_parse_a_select_with_a_column_alias() {
        assertParsing("SELECT a AS b FROM test",

                select(List.of(columnAlias(unqualifiedColumnName("a"), "b")),
                        tableName("test"),
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void it_should_parse_a_parenthesized_column_reference() {
        assertParsing("SELECT (a) FROM test",

                select(columns("a"), tableName("test"), null, null, null, null));
    }

    @Test
    void it_should_parse_boolean_constants_in_the_output_columns() {
        assertParsing("SELECT true, false FROM test",

                select(List.of(TRUE, FALSE), tableName("test"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_select_query_with_an_order_by_clause() {
        assertParsing("SELECT column1 FROM test ORDER BY column1 ASC, column2, column3 DESC",

                select(columns("column1"),
                        tableName("test"),
                        null,
                        null,
                        null,
                        sortSpecificationList(List.of(
                                sortSpecification(unqualifiedColumnName("column1"), ASCENDING),
                                sortSpecification(unqualifiedColumnName("column2"), ASCENDING),
                                sortSpecification(unqualifiedColumnName("column3"), DESCENDING)))));
    }

    @Test
    void it_should_parse_a_select_query_with_several_tables() {
        assertParsing("SELECT * FROM one, two, three",

                select(List.of(asterisk()),
                        tableReferenceList(tableName("one"),
                                tableName("two"),
                                List.of(tableName("three"))),
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void it_should_parse_an_inner_join_in_a_select_query() {
        assertParsing("SELECT * FROM table1 INNER JOIN table2 on column1 = column2",

                select(List.of(asterisk()),
                        innerJoin(tableName("table1"),
                                tableName("table2"),
                                equal(unqualifiedColumnName("column1"),
                                        unqualifiedColumnName("column2"))),
                        null,
                        null,
                        null,
                        null));
    }

    @Test
    void it_should_parse_a_select_query_with_a_limit() {
        assertParsing("SELECT * FROM test LIMIT 3",

                limit(select(List.of(asterisk()), tableName("test"), null, null, null, null), 3));
    }

    @Test
    void it_should_parse_a_qualified_column_reference() {
        assertParsing("SELECT test.a FROM test",

                select(List.of(qualifiedColumnName("test", "a")), tableName("test"), null, null, null, null));
    }

    @Test
    void it_should_parse_the_substring_method() {
        assertParsing("VALUES (SUBSTRING ('caca' FROM 0 FOR 2))",

                rows(row(substring(textLiteral("caca"), integerLiteral(0), integerLiteral(2)))));
    }

    @Test
    void it_should_parse_a_select_from_values() {
        assertParsing("SELECT * FROM VALUES ()",

                select(List.of(asterisk()), rows(row()), null, null, null, null));
    }
}
