package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.value.GreaterOrEqual;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.GroupingSetsList.groupingSetsList;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class GroupByParserTest {

    @Test
    void it_should_parse_a_query_grouped_by_a_single_column() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a",

                select()
                        .outputColumns(countStar())
                        .fromClause(TableName.tableName("test"))
                        .whereClause(null)
                        .groupByClause(groupingSetsList(List.of(unqualifiedColumnName("a"))))
                        .havingClause(null)
                        .orderByClause(null));
    }

    @Test
    void it_should_parse_a_query_grouped_by_several_columns() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a, b, c",

                select()
                        .outputColumns(countStar())
                        .fromClause(TableName.tableName("test"))
                        .groupByClause(groupingSetsList(List.of(unqualifiedColumnName("a"),
                                unqualifiedColumnName("b"),
                                unqualifiedColumnName("c")))));
    }

    @Test
    void it_should_parse_a_query_with_a_having_filter_in_the_group_by_clause() {
        assertParsing("SELECT a FROM test GROUP BY a HAVING count(*) >= 2",

                select()
                        .outputColumns(unqualifiedColumnName("a"))
                        .fromClause(TableName.tableName("test"))
                        .groupByClause(groupingSetsList(List.of(unqualifiedColumnName("a"))))
                        .havingClause(GreaterOrEqual.greaterOrEqual(countStar(), IntegerLiteral.integerLiteral(2))));
    }
}
