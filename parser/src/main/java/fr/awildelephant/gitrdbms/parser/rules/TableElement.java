package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.ColumnDefinition;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import static fr.awildelephant.gitrdbms.ast.ColumnDefinition.columnDefinition;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.INTEGER;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableElement {

    private TableElement() {

    }

    static ColumnDefinition deriveTableElement(final Lexer lexer) {
        final String columnName = consumeIdentifier(lexer);

        consumeAndExpect(lexer, INTEGER);

        return columnDefinition(columnName, ColumnDefinition.INTEGER);
    }
}
