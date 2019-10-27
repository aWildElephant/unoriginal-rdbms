package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnReference;
import fr.awildelephant.rdbms.lexer.InputStreamWrapper;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.UnqualifiedColumnReference.unqualifiedColumnReference;
import static org.assertj.core.api.Assertions.assertThat;

final class ParserTestHelper {

    private ParserTestHelper() {

    }

    static void assertParsing(String input, AST expectedAST) {
        assertThat(new Parser(new Lexer(InputStreamWrapper.wrap(input))).parse()).isEqualTo(expectedAST);
    }

    static List<ColumnReference> columns(String... names) {
        final List<ColumnReference> columnNames = new ArrayList<>(names.length);

        for (String name : names) {
            columnNames.add(unqualifiedColumnReference(name));
        }

        return columnNames;
    }
}
