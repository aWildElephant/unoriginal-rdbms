package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.columnDefinition;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NOT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableElement {

    private TableElement() {

    }

    static ColumnDefinition deriveTableElement(final Lexer lexer) {
        final String columnName = consumeIdentifier(lexer);

        final Token columnTypeToken = lexer.consumeNextToken();

        boolean notNull = false;
        if (lexer.lookupNextToken().type() == NOT) {
            lexer.consumeNextToken();
            consumeAndExpect(NULL, lexer);

            notNull = true;
        }

        return columnDefinition(columnName, columnType(columnTypeToken), notNull);
    }

    private static int columnType(Token columnTypeToken) {
        switch (columnTypeToken.type()) {
            case DECIMAL:
                return ColumnDefinition.DECIMAL;
            case INTEGER:
                return ColumnDefinition.INTEGER;
            case TEXT:
                return ColumnDefinition.TEXT;
            default:
                throw unexpectedToken(columnTypeToken);
        }
    }

}
