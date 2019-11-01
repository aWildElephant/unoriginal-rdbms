package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.IntervalGranularity.DAY_GRANULARITY;
import static fr.awildelephant.rdbms.ast.value.IntervalGranularity.YEAR_GRANULARITY;
import static fr.awildelephant.rdbms.ast.value.IntervalLiteral.intervalLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class IntervalLiteralParserTest {

    @Test
    void it_should_parse_an_interval_literal_with_the_precision_field() {
        assertParsing("VALUES (INTERVAL '7' DAY (3))",

                      rows(row(intervalLiteral("7", DAY_GRANULARITY, 3))));
    }

    @Test
    void it_should_parse_an_interval_literal_without_the_precision_field() {
        assertParsing("VALUES (INTERVAL '7' DAY)",

                      rows(row(intervalLiteral("7", DAY_GRANULARITY, null))));
    }

    @Test
    void it_should_parse_an_interval_literal_with_year() {
        assertParsing("VALUES (INTERVAL '3' YEAR)",

                      rows(row(intervalLiteral("3", YEAR_GRANULARITY, null))));
    }
}
