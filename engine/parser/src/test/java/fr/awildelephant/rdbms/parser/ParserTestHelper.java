package fr.awildelephant.rdbms.parser;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.lexer.InputStreamWrapper;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.utils.common.Builder;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static org.assertj.core.api.Assertions.assertThat;

final class ParserTestHelper {

    private ParserTestHelper() {

    }

    static void assertParsing(String input, Builder<? extends AST> expectedASTBuilder) {
        assertParsing(input, expectedASTBuilder.build());
    }

    static void assertParsing(String input, AST expectedAST) {
        assertThat(new Parser(new Lexer(InputStreamWrapper.wrap(input))).parse()).isEqualTo(expectedAST);
    }

    static List<ColumnName> columns(String... names) {
        final List<ColumnName> columnNames = new ArrayList<>(names.length);

        for (String name : names) {
            columnNames.add(unqualifiedColumnName(name));
        }

        return columnNames;
    }
}
