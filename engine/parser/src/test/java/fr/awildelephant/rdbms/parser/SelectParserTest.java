package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.IdentifierChain.identifierChain;
import static fr.awildelephant.rdbms.ast.InnerJoin.innerJoin;
import static fr.awildelephant.rdbms.ast.Limit.limit;
import static fr.awildelephant.rdbms.ast.OrderingSpecification.ASCENDING;
import static fr.awildelephant.rdbms.ast.OrderingSpecification.DESCENDING;
import static fr.awildelephant.rdbms.ast.SortSpecification.sortSpecification;
import static fr.awildelephant.rdbms.ast.SortSpecificationList.sortSpecificationList;
import static fr.awildelephant.rdbms.ast.SortedSelect.select;
import static fr.awildelephant.rdbms.ast.SortedSelect.sortedSelect;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.FALSE;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.TRUE;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.identifierChain;

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

                      select(List.of(asterisk()), tableName("z")));
    }

    @Test
    void it_should_parse_a_select_distinct() {
        assertParsing("SELECT DISTINCT a FROM test",

                      distinct(select(columns("a"), tableName("test"))));
    }

    @Test
    void it_should_parse_a_select_with_a_column_alias() {
        assertParsing("SELECT a AS b FROM test",

                      select(List.of(columnAlias(identifierChain("a"), "b")), tableName("test")));
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
        assertParsing("SELECT column1 FROM test ORDER BY column1 ASC, column2, column3 DESC",

                      sortedSelect(columns("column1"),
                                   sortSpecificationList(
                                           List.of(sortSpecification(identifierChain("column1"), ASCENDING),
                                                   sortSpecification(identifierChain("column2"), ASCENDING),
                                                   sortSpecification(identifierChain("column3"), DESCENDING))),
                                   tableName("test")));
    }

    @Test
    void it_should_parse_a_select_query_with_several_tables() {
        assertParsing("SELECT * FROM one, two, three",

                      select(List.of(asterisk()),
                             tableReferenceList(tableName("one"),
                                                tableName("two"),
                                                List.of(tableName("three")))));
    }

    @Test
    void it_should_parse_an_inner_join_in_a_select_query() {
        assertParsing("SELECT * FROM table1 INNER JOIN table2 on column1 = column2",

                      select(List.of(asterisk()),
                             innerJoin(tableName("table1"),
                                       tableName("table2"),
                                       equal(identifierChain("column1"),
                                             identifierChain("column2")))));
    }

    @Test
    void it_should_parse_a_select_query_with_a_limit() {
        assertParsing("SELECT * FROM test LIMIT 3",

                      limit(select(List.of(asterisk()), tableName("test")), 3));
    }

    @Test
    void it_should_parse_a_qualified_column_reference() {
        assertParsing("SELECT test.a FROM test",

                      select(List.of(identifierChain("test", "a")), tableName("test")));
    }
}
