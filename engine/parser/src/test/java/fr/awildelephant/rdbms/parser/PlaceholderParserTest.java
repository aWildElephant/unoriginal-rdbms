package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;
import static fr.awildelephant.rdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.builder.SelectBuilder.select;
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

                select().outputColumns(asterisk())
                        .fromClause(tableName("test"))
                        .whereClause(equal(unqualifiedColumnName("a"), placeholder())));
    }
}
