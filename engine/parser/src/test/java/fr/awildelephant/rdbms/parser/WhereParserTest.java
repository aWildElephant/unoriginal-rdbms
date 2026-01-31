package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class WhereParserTest {

    @Test
    void it_should_parse_a_select_query_with_a_where_clause() {
        assertParsing("SELECT * FROM employee WHERE surname = 'Girard'",

                select().fromClause(tableName("employee"))
                        .whereClause(equal(unqualifiedColumnName("surname"), textLiteral("Girard"))));
    }
}
