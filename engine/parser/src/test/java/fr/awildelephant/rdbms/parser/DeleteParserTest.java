package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Delete.delete;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class DeleteParserTest {

    @Test
    void it_should_parse_a_delete_statement() {
        assertParsing("DELETE FROM t1 WHERE c1 = 'value'",

                delete(tableName("t1"), equal(unqualifiedColumnName("c1"), textLiteral("value"))));
    }
}
