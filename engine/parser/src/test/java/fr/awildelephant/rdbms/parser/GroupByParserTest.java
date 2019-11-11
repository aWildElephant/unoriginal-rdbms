package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.GroupBy.groupBy;
import static fr.awildelephant.rdbms.ast.GroupingSetsList.groupingSetsList;
import static fr.awildelephant.rdbms.ast.Having.having;
import static fr.awildelephant.rdbms.ast.SortedSelect.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.GreaterOrEqual.greaterOrEqual;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;

class GroupByParserTest {

    @Test
    void it_should_parse_a_query_grouped_by_a_single_column() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a",

                      select(List.of(countStar()),
                             groupBy(tableName("test"), groupingSetsList(List.of(unqualifiedColumnName("a"))))));
    }

    @Test
    void it_should_parse_a_query_grouped_by_several_columns() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a, b, c",

                      select(List.of(countStar()),
                             groupBy(tableName("test"), groupingSetsList(columns("a", "b", "c")))));
    }

    @Test
    void it_should_parse_a_query_with_a_having_filter_in_the_group_by_clause() {
        assertParsing("SELECT a FROM test GROUP BY a HAVING count(*) >= 2",

                      select(columns("a"),
                             having(groupBy(tableName("test"), groupingSetsList(columns("a"))),
                                    greaterOrEqual(countStar(), integerLiteral(2)))));
    }
}
