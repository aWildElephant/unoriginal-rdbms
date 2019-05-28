package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.SortedSelect.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class AggregateParserTest {

    @Test
    void it_should_parse_a_count_star_aggregate() {
        assertParsing("SELECT COUNT(*) FROM test",

                      select(List.of(countStar()), tableName("test")));
    }

    @Test
    void it_should_parse_a_count_star_aggregate_within_a_numeric_value_expression() {
        assertParsing("SELECT COUNT(*) * 2 FROM test",

                      select(List.of(multiply(countStar(), integerLiteral(2))), tableName("test")));
    }
}
