package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.ast.TableAliasWithColumns.tableAliasWithColumns;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class TableAliasParserTest {

    @Test
    void it_should_parse_a_select_query_with_a_table_alias() {
        assertParsing("SELECT * FROM test AS t",

                      select(List.of(asterisk()), tableAlias(tableName("test"), "t"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_select_query_with_a_table_alias_without_the_as_keyword() {
        assertParsing("SELECT * FROM test t",

                      select(List.of(asterisk()), tableAlias(tableName("test"), "t"), null, null, null, null));
    }

    @Test
    void it_should_parse_a_select_query_with_a_table_alias_that_has_column_aliases() {
        assertParsing("SELECT * FROM test AS t (a, b, c)",

                      select(List.of(asterisk()),
                             tableAliasWithColumns(tableName("test"), "t", List.of("a", "b", "c")),
                             null,
                             null,
                             null,
                             null));
    }
}
