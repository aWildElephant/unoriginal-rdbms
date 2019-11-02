package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.SortedSelect.select;
import static fr.awildelephant.rdbms.ast.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class TableAliasParserTest {

    @Test
    void it_should_parse_a_select_query_with_a_table_alias() {
        assertParsing("SELECT * FROM test AS t",

                      select(List.of(asterisk()), tableAlias(tableName("test"), "t")));
    }

    @Test
    void it_should_parse_a_select_query_with_a_table_alias_without_the_as_keyword() {
        assertParsing("SELECT * FROM test t",

                      select(List.of(asterisk()), tableAlias(tableName("test"), "t")));
    }
}
