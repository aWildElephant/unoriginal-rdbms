package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.*;
import static fr.awildelephant.rdbms.ast.CreateTable.createTable;
import static fr.awildelephant.rdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;
import static java.util.Collections.singletonList;

class CreateTableParserTest {

    @Test
    void it_should_parse_a_create_table_query_with_a_single_column() {
        final List<ColumnDefinition> columns = new ArrayList<>();
        columns.add(builder("a", INTEGER).build());

        assertParsing("CREATE TABLE test (a INTEGER)", createTable(tableName("test"), tableElementList(columns)));
    }

    @Test
    void it_should_parse_a_create_table_query_with_several_columns() {
        final ArrayList<ColumnDefinition> columns = new ArrayList<>();
        columns.add(builder("a", INTEGER).build());
        columns.add(builder("b", INTEGER).build());
        columns.add(builder("c", INTEGER).build());

        assertParsing("CREATE TABLE test (a INTEGER, b INTEGER, c INTEGER)", createTable(tableName("test"), tableElementList(columns)));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_text_column() {
        final List<ColumnDefinition> columns = new ArrayList<>();
        columns.add(builder("a", INTEGER).build());
        columns.add(builder("b", TEXT).build());

        assertParsing("CREATE TABLE test (a INTEGER, b TEXT)", createTable(tableName("test"), tableElementList(columns)));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_decimal_column() {
        final List<ColumnDefinition> columns = new ArrayList<>();
        columns.add(builder("a", INTEGER).build());
        columns.add(builder("b", DECIMAL).build());

        assertParsing("CREATE TABLE test (a INTEGER, b DECIMAL)", createTable(tableName("test"), tableElementList(columns)));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_not_null_constraint() {
        final List<ColumnDefinition> columns = singletonList(builder("a", INTEGER).notNull().build());

        assertParsing("CREATE TABLE test (a INTEGER NOT NULL)", createTable(tableName("test"), tableElementList(columns)));
    }

    @Test
    void it_should_parse_a_create_table_query_with_an_unique_constraint() {
        final List<ColumnDefinition> columns = singletonList(builder("a", INTEGER).unique().build());

        assertParsing("CREATE TABLE test (a INTEGER UNIQUE)", createTable(tableName("test"), tableElementList(columns)));
    }
}
