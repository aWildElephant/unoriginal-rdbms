package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.ast.SortedSelect.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.Where.where;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.TextLiteral.textLiteral;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class WhereParserTest {

    @Test
    void it_should_parse_a_select_query_with_a_where_clause() {
        assertParsing("SELECT * FROM employee WHERE surname = 'Girard'",

                      select(List.of(asterisk()),
                             where(tableName("employee"),
                                   equal(columnName("surname"), textLiteral("Girard")))));
    }
}
