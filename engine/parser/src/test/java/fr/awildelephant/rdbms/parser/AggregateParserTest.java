package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.Any.any;
import static fr.awildelephant.rdbms.ast.value.Count.count;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.Min.min;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class AggregateParserTest {

    @Test
    void it_should_parse_a_count_star_aggregate() {
        assertParsing("SELECT COUNT(*) FROM test",

                      select(List.of(countStar()), tableName("test"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_count_star_aggregate_within_a_numeric_value_expression() {
        assertParsing("SELECT COUNT(*) * 2 FROM test",

                      select(List.of(multiply(countStar(), integerLiteral(2))),
                             tableName("test"),
                             null,
                             null,
                             null,
                             null));
    }

    @Test
    void it_should_parse_a_min_aggregate() {
        assertParsing("SELECT MIN(a) FROM test",

                      select(List.of(min(unqualifiedColumnName("a"))), tableName("test"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_count_distinct_aggregate() {
        assertParsing("SELECT COUNT(DISTINCT a) FROM test",

                      select(List.of(count(true, unqualifiedColumnName("a"))),
                             tableName("test"),
                             null,
                             null,
                             null,
                             null));
    }

    @Test
    void it_should_parse_an_any_aggregate() {
        assertParsing("SELECT ANY(a) FROM test",

                      select(List.of(any(unqualifiedColumnName("a"))), tableName("test"), null, null, null, null));
    }
}
