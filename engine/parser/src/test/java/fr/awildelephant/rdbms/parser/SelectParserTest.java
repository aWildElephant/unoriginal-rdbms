package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.TableReferenceList;
import fr.awildelephant.rdbms.ast.Values;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.InnerJoin.innerJoin;
import static fr.awildelephant.rdbms.ast.Limit.limit;
import static fr.awildelephant.rdbms.ast.QualifiedColumnName.qualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.SortSpecification.sortSpecification;
import static fr.awildelephant.rdbms.ast.SortSpecificationList.sortSpecificationList;
import static fr.awildelephant.rdbms.ast.Substring.substring;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.ast.ordering.NullsHandling.NULLS_FIRST;
import static fr.awildelephant.rdbms.ast.ordering.NullsHandling.NULLS_LAST;
import static fr.awildelephant.rdbms.ast.ordering.OrderingSpecification.ASCENDING;
import static fr.awildelephant.rdbms.ast.ordering.OrderingSpecification.DESCENDING;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.falseLiteral;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.trueLiteral;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class SelectParserTest {

    @Test
    void it_should_parse_a_select_statement_with_a_single_columns() {
        assertParsing("SELECT y FROM z",

                select()
                        .outputColumns(unqualifiedColumnName("y"))
                        .fromClause(TableName.tableName("z")));
    }

    @Test
    void it_should_parse_a_select_statement_with_several_output_columns() {
        assertParsing("SELECT w, x, y FROM z",

                select()
                        .outputColumns(unqualifiedColumnName("w"), unqualifiedColumnName("x"), unqualifiedColumnName("y"))
                        .fromClause(TableName.tableName("z")));
    }

    @Test
    void it_should_parse_a_select_star_statement() {
        assertParsing("SELECT * FROM z",

                select()
                        .outputColumns(asterisk())
                        .fromClause(TableName.tableName("z")));
    }

    @Test
    void it_should_parse_a_select_distinct() {
        assertParsing("SELECT DISTINCT a FROM test",

                distinct(select()
                        .outputColumns(unqualifiedColumnName("a"))
                        .fromClause(TableName.tableName("test")).build()));
    }

    @Test
    void it_should_parse_a_select_with_a_column_alias() {
        assertParsing("SELECT a AS b FROM test",

                select()
                        .outputColumns(columnAlias(unqualifiedColumnName("a"), "b"))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_a_parenthesized_column_reference() {
        assertParsing("SELECT (a) FROM test",

                select()
                        .outputColumns(unqualifiedColumnName("a"))
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_boolean_constants_in_the_output_columns() {
        assertParsing("SELECT true, false FROM test",

                select()
                        .outputColumns(trueLiteral(), falseLiteral())
                        .fromClause(TableName.tableName("test")));
    }

    @Test
    void it_should_parse_a_select_query_with_an_order_by_clause() {
        assertParsing("""
                        SELECT column1
                        FROM test
                        ORDER BY
                            column1 ASC,
                            column2,
                            column3 DESC,
                            column4 NULLS FIRST,
                            column5 NULLS LAST,
                            column6 DESC NULLS FIRST,
                            column7 DESC NULLS LAST
                        """,

                select()
                        .outputColumns(unqualifiedColumnName("column1"))
                        .fromClause(TableName.tableName("test"))
                        .orderByClause(sortSpecificationList(List.of(
                                sortSpecification(unqualifiedColumnName("column1"), ASCENDING),
                                sortSpecification(unqualifiedColumnName("column2"), ASCENDING),
                                sortSpecification(unqualifiedColumnName("column3"), DESCENDING),
                                sortSpecification(unqualifiedColumnName("column4"), ASCENDING, NULLS_FIRST),
                                sortSpecification(unqualifiedColumnName("column5"), ASCENDING, NULLS_LAST),
                                sortSpecification(unqualifiedColumnName("column6"), DESCENDING, NULLS_FIRST),
                                sortSpecification(unqualifiedColumnName("column7"), DESCENDING, NULLS_LAST)))));
    }

    @Test
    void it_should_parse_a_select_query_with_several_tables() {
        assertParsing("SELECT * FROM one, two, three",

                select()
                        .outputColumns(asterisk())
                        .fromClause(TableReferenceList.tableReferenceList(TableName.tableName("one"),
                                TableName.tableName("two"),
                                List.of(TableName.tableName("three")))));
    }

    @Test
    void it_should_parse_an_inner_join_in_a_select_query() {
        assertParsing("SELECT * FROM table1 INNER JOIN table2 on column1 = column2",

                select()
                        .outputColumns(asterisk())
                        .fromClause(innerJoin(tableName("table1"),
                                tableName("table2"),
                                equal(unqualifiedColumnName("column1"),
                                        unqualifiedColumnName("column2")))));
    }

    @Test
    void it_should_parse_a_select_query_with_a_limit() {
        assertParsing("SELECT * FROM test LIMIT 3",

                limit(select()
                        .outputColumns(asterisk())
                        .fromClause(tableName("test")).build(), 3));
    }

    @Test
    void it_should_parse_a_qualified_column_reference() {
        assertParsing("SELECT test.a FROM test",

                select()
                        .outputColumns(qualifiedColumnName("test", "a"))
                        .fromClause(tableName("test")));
    }

    @Test
    void it_should_parse_the_substring_method() {
        assertParsing("VALUES (SUBSTRING ('caca' FROM 0 FOR 2))",

                rows(row(substring(textLiteral("caca"), integerLiteral(0), integerLiteral(2)))));
    }

    @Test
    void it_should_parse_a_select_from_values() {
        assertParsing("SELECT * FROM VALUES ()",

                select()
                        .outputColumns(asterisk())
                        .fromClause(Values.rows(Row.row())));
    }
}
