package fr.awildelephant.rdbms.parser;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Rows.rows;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

class InsertIntoParserTest {

    @Test
    void it_should_parse_an_insert_into_statement_with_a_single_column_and_a_single_row() {

        ParserTestHelper.assertParsing("INSERT INTO z VALUES (1)", insertInto(tableName("z"), rows(singletonList(row(singletonList(1))))));
    }

    @Test
    void it_should_parse_an_insert_into_statement_with_several_columns_and_a_single_row() {
        ParserTestHelper.assertParsing("INSERT INTO z VALUES (1, 2, 3)", insertInto(tableName("z"), rows(singletonList(row(asList(1, 2, 3))))));
    }
}
