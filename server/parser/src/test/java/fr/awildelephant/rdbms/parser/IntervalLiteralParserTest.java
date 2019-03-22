package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.Interval.interval;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class IntervalLiteralParserTest {

    @Test
    void it_should_parse_an_interval_literal_with_the_precision_field() {
        assertParsing("VALUES (INTERVAL '7' DAY (3))",

                      rows(row(interval("7", 3))));
    }

    @Test
    void it_should_parse_an_interval_literal_without_the_precision_field() {
        assertParsing("VALUES (INTERVAL '7' DAY)",

                      rows(row(interval("7", null))));
    }
}
