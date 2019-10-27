package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.SortedSelect.select;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnReference.unqualifiedColumnReference;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.Where.where;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Placeholder.placeholder;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class PlaceholderParserTest {

    @Test
    void it_should_parse_an_insert_statement_with_placeholders() {
        assertParsing("INSERT INTO test VALUES (?, ?)",

                      insertInto(tableName("test"), rows(row(placeholder(), placeholder()))));
    }

    @Test
    void it_should_parse_a_select_statement_with_a_placeholder_in_the_where_clause() {
        assertParsing("SELECT * FROM test WHERE a = ?",

                      select(List.of(asterisk()),
                             where(tableName("test"), equal(unqualifiedColumnReference("a"), placeholder()))));
    }
}
