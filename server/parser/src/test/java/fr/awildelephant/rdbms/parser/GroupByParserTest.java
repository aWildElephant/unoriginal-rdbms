package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.ast.GroupBy.groupBy;
import static fr.awildelephant.rdbms.ast.GroupingSetsList.groupingSetsList;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.columns;

class GroupByParserTest {

    @Test
    void it_should_parse_a_query_grouped_by_a_single_column() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a",

                      select(List.of(countStar()), groupBy(tableName("test"), groupingSetsList(List.of(columnName("a"))))));
    }

    @Test
    void it_should_parse_a_query_grouped_by_several_columns() {
        assertParsing("SELECT COUNT(*) FROM test GROUP BY a, b, c",

                      select(List.of(countStar()), groupBy(tableName("test"), groupingSetsList(columns("a", "b", "c")))));
    }
}
