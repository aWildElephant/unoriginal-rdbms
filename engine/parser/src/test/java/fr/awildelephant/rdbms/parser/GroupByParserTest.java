package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.GroupingSetsList.groupingSetsList;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.GreaterOrEqual.greaterOrEqual;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class GroupByParserTest {

    @Test
    void it_should_parse_a_query_grouped_by_a_single_column() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a",

                      select(List.of(countStar()),
                             tableName("test"),
                             null,
                             groupingSetsList(List.of(unqualifiedColumnName("a"))),
                             null,
                             null));
    }

    @Test
    void it_should_parse_a_query_grouped_by_several_columns() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a, b, c",

                      select(List.of(countStar()),
                             tableName("test"),
                             null,
                             groupingSetsList(List.of(unqualifiedColumnName("a"),
                                                      unqualifiedColumnName("b"),
                                                      unqualifiedColumnName("c"))),
                             null,
                             null));
    }

    @Test
    void it_should_parse_a_query_with_a_having_filter_in_the_group_by_clause() {
        assertParsing("SELECT a FROM test GROUP BY a HAVING count(*) >= 2",

                      select(List.of(unqualifiedColumnName("a")),
                             tableName("test"),
                             null,
                             groupingSetsList(List.of(unqualifiedColumnName("a"))),
                             greaterOrEqual(countStar(), integerLiteral(2)),
                             null));
    }
}
