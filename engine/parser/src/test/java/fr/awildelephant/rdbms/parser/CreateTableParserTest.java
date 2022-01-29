package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.TableElementList;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static fr.awildelephant.rdbms.ast.ColumnType.*;
import static fr.awildelephant.rdbms.ast.CreateTable.createTable;
import static fr.awildelephant.rdbms.ast.TableElementList.tableElementList;
import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class CreateTableParserTest {

    @Test
    void it_should_parse_a_create_table_query_with_a_single_column() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .build();

        assertParsing("CREATE TABLE test (a INTEGER)", createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_several_columns() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addColumn("b", INTEGER)
                .addColumn("c", INTEGER)
                .build();

        assertParsing("CREATE TABLE test (a INTEGER, b INTEGER, c INTEGER)", createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_text_column() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addColumn("b", TEXT)
                .build();

        assertParsing("CREATE TABLE test (a INTEGER, b TEXT)", createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_decimal_column() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addColumn("b", DECIMAL)
                .build();

        assertParsing("CREATE TABLE test (a INTEGER, b DECIMAL)", createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_not_null_constraint_on_a_column() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addNotNullConstraint("a")
                .build();

        assertParsing("CREATE TABLE test (a INTEGER NOT NULL)", createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_an_unique_constraint_on_a_column() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addUniqueConstraint("a")
                .build();

        assertParsing("CREATE TABLE test (a INTEGER UNIQUE)", createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_an_unique_constraint_definition() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addUniqueConstraint("a")
                .build();

        assertParsing("CREATE TABLE test (a INTEGER, UNIQUE(a))", createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_an_unique_constraint_definition_on_several_columns() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addColumn("b", INTEGER)
                .addUniqueConstraint(Set.of("a", "b"))
                .build();

        assertParsing("CREATE TABLE test (a INTEGER, b INTEGER, UNIQUE(a, b))",

                createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_foreign_key_constraint() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addForeignKeyConstraint(Set.of("a"), "other", Set.of("a"))
                .build();

        assertParsing("CREATE TABLE test (a INTEGER REFERENCES other(a))",

                createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_foreign_key_constraint_definition_on_several_columns() {
        final TableElementList elements = tableElementList().addColumn("a", INTEGER)
                .addColumn("b", INTEGER)
                .addForeignKeyConstraint(Set.of("a", "b"),
                        "other",
                        Set.of("c", "d"))
                .build();

        assertParsing("CREATE TABLE test (a INTEGER, b INTEGER, FOREIGN KEY (a, b) REFERENCES other(c, d))",

                createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_fixed_length_char_type() {
        final TableElementList elements = tableElementList().addColumn("a", TEXT)
                .build();

        assertParsing("CREATE TABLE test (a CHAR(8))",

                createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_variable_length_char_type() {
        final TableElementList elements = tableElementList().addColumn("a", TEXT)
                .build();

        assertParsing("CREATE TABLE test (a VARCHAR(32))",

                createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_date_column() {
        final TableElementList elements = tableElementList().addColumn("a", DATE)
                .build();

        assertParsing("CREATE TABLE test (a DATE)",

                createTable(tableName("test"), elements));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_boolean_column() {
        final TableElementList elements = tableElementList().addColumn("a", BOOLEAN)
                .build();

        assertParsing("CREATE TABLE test (a BOOLEAN)",

                createTable(tableName("test"), elements));
    }
}
