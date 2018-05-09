package fr.awildelephant.gitrdbms.parser;

import fr.awildelephant.gitrdbms.lexer.InputStreamWrapper;
import fr.awildelephant.gitrdbms.lexer.Lexer;
import fr.awildelephant.gitrdbms.parser.ast.AST;
import fr.awildelephant.gitrdbms.parser.ast.ColumnDefinition;
import fr.awildelephant.gitrdbms.parser.ast.CreateTable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static fr.awildelephant.gitrdbms.parser.ast.ColumnDefinition.INTEGER;
import static org.assertj.core.api.Assertions.assertThat;

class CreateTableParserTest {

    @Test
    void it_should_parse_a_create_table_query_with_no_column() {
        assertParsing("CREATE TABLE z ()", new CreateTable("z", Collections.emptyList()));
    }

    @Test
    void it_should_parse_a_create_table_query_with_a_single_column() {
        final ArrayList<ColumnDefinition> columns = new ArrayList<>();
        columns.add(new ColumnDefinition("y", INTEGER));

        assertParsing("CREATE TABLE z (y INTEGER)", new CreateTable("z", columns));
    }

    private void assertParsing(String input, AST expectedAST) {
        assertThat(new Parser(new Lexer(InputStreamWrapper.wrap(input))).parse()).isEqualTo(expectedAST);
    }
}
