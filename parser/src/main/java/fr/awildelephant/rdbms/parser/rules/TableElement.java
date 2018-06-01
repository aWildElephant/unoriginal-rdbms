package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.columnDefinition;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableElement {

    private TableElement() {

    }

    static ColumnDefinition deriveTableElement(final Lexer lexer) {
        final String columnName = consumeIdentifier(lexer);

        consumeAndExpect(lexer, INTEGER);

        return columnDefinition(columnName, ColumnDefinition.INTEGER);
    }
}
