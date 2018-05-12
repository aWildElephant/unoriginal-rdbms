package fr.awildelephant.gitrdbms.parser;

import fr.awildelephant.gitrdbms.ast.ColumnDefinition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.gitrdbms.ast.ColumnDefinition.INTEGER;
import static fr.awildelephant.gitrdbms.ast.ColumnDefinition.columnDefinition;
import static fr.awildelephant.gitrdbms.ast.CreateTable.createTable;
import static fr.awildelephant.gitrdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.gitrdbms.ast.TableName.tableName;
import static fr.awildelephant.gitrdbms.parser.ParserTestHelper.assertParsing;
import static java.util.Collections.emptyList;

class CreateTableParserTest {

    @Test
    void it_should_parse_a_create_table_query_with_a_single_column() {
        final List<ColumnDefinition> columns = new ArrayList<>();
        columns.add(columnDefinition("y", INTEGER));

        assertParsing("CREATE TABLE z (y INTEGER)", createTable(tableName("z"), tableElementList(columns)));
    }

    @Test
    void it_should_parse_a_create_table_query_with_several_columns() {
        final ArrayList<ColumnDefinition> columns = new ArrayList<>();
        columns.add(columnDefinition("y", INTEGER));
        columns.add(columnDefinition("x", INTEGER));
        columns.add(columnDefinition("w", INTEGER));

        assertParsing("CREATE TABLE z (y INTEGER, x INTEGER, w INTEGER)", createTable(tableName("z"), tableElementList(columns)));
    }

}
