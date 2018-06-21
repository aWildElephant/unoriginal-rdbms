package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Rows.rows;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

class InsertIntoParserTest {

    @Test
    void it_should_parse_an_insert_into_statement_with_a_single_column_and_a_single_row() {
        assertParsing("INSERT INTO test VALUES (1)", insertInto(tableName("test"), rows(singletonList(row(singletonList(1))))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_several_columns_and_a_single_row() {
        assertParsing("INSERT INTO test VALUES (1, 2, 3)", insertInto(tableName("test"), rows(singletonList(row(asList(1, 2, 3))))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_a_text_literal() {
        assertParsing("INSERT INTO test VALUES ('some text')", insertInto(tableName("test"), rows(singletonList(row(singletonList("some text"))))));
    }
}
