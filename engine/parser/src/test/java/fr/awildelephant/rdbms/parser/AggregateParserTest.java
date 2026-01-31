package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
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

                select().outputColumns(countStar()).fromClause(tableName("test")));
    }

    @Test
    void it_should_parse_a_count_star_aggregate_within_a_numeric_value_expression() {
        assertParsing("SELECT COUNT(*) * 2 FROM test",

                select().outputColumns(multiply(countStar(), integerLiteral(2)))
                        .fromClause(tableName("test")));
    }

    @Test
    void it_should_parse_a_min_aggregate() {
        assertParsing("SELECT MIN(a) FROM test",

                select().outputColumns(min(unqualifiedColumnName("a")))
                        .fromClause(tableName("test")));
    }

    @Test
    void it_should_parse_a_count_distinct_aggregate() {
        assertParsing("SELECT COUNT(DISTINCT a) FROM test",

                select().outputColumns(count(true, unqualifiedColumnName("a")))
                        .fromClause(tableName("test")));
    }
}
