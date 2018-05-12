package fr.awildelephant.gitrdbms.parser;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.ast.ColumnName;
import fr.awildelephant.gitrdbms.lexer.InputStreamWrapper;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.gitrdbms.ast.ColumnName.columnName;
import static org.assertj.core.api.Assertions.assertThat;

final class ParserTestHelper {

    private ParserTestHelper() {

    }

    static void assertParsing(String input, AST expectedAST) {
        assertThat(new Parser(new Lexer(InputStreamWrapper.wrap(input))).parse()).isEqualTo(expectedAST);
    }

    static List<ColumnName> columns(String... names) {
        ArrayList<ColumnName> columnNames = new ArrayList(names.length);

        for (String name : names) {
            columnNames.add(columnName(name));
        }

        return columnNames;
    }
}
