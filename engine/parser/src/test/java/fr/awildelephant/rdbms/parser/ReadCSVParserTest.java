package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.ColumnType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.column;
import static fr.awildelephant.rdbms.ast.ReadCSV.readCSV;
import static fr.awildelephant.rdbms.parser.ParserTestHelper.assertParsing;

class ReadCSVParserTest {

    @Test
    void it_should_parse_a_read_csv_statement_with_no_column() {
        assertParsing("READ CSV '/path/to/file.csv' ()",

                readCSV("/path/to/file.csv", List.of()));
    }

    @Test
    void it_should_parse_a_read_csv_statement_with_one_column() {
        assertParsing("READ CSV '/path/to/file.csv' (id INTEGER)",

                readCSV("/path/to/file.csv", List.of(column("id", ColumnType.INTEGER))));
    }

    @Test
    void it_should_parse_a_read_csv_statement_with_two_columns() {
        assertParsing("READ CSV '/path/to/file.csv' (id INTEGER, name TEXT)",

                readCSV("/path/to/file.csv", List.of(column("id", ColumnType.INTEGER), column("name", ColumnType.TEXT))));
    }
}
