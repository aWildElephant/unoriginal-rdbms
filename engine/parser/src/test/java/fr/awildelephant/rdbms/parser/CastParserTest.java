package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.ColumnType;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class CastParserTest {

    @Test
    void it_should_parse_a_cast_expression() {
        assertParsing("VALUES (CAST('2' AS INTEGER))",

                rows(row(cast(textLiteral("2"), ColumnType.INTEGER))));
    }
}
