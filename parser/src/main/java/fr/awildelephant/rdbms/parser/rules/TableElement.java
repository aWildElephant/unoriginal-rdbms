package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.columnDefinition;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableElement {

    private TableElement() {

    }

    static ColumnDefinition deriveTableElement(final Lexer lexer) {
        final String columnName = consumeIdentifier(lexer);

        final Token columnTypeToken = lexer.consumeNextToken();

        switch (columnTypeToken.type()) {
            case DECIMAL:
                return columnDefinition(columnName, ColumnDefinition.DECIMAL);
            case INTEGER:
                return columnDefinition(columnName, ColumnDefinition.INTEGER);
            case TEXT:
                return columnDefinition(columnName, ColumnDefinition.TEXT);
            default:
                throw unexpectedToken(columnTypeToken);
        }
    }
}
