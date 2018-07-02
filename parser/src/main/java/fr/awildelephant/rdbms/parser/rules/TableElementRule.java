package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ColumnConstraintDefinitionsRule.deriveColumnConstraintDefinitions;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableElementRule {

    private TableElementRule() {

    }

    static void deriveTableElement(TableElementList.Builder tableElementListBuilder, final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        switch (token.type()) {
            case UNIQUE:
                lexer.consumeNextToken();

                consumeAndExpect(LEFT_PAREN, lexer);

                tableElementListBuilder.addUniqueConstraint(consumeIdentifier(lexer));

                consumeAndExpect(RIGHT_PAREN, lexer);
                break;

            default:
                final String columnName = consumeIdentifier(lexer);
                final Token columnTypeToken = lexer.consumeNextToken();

                tableElementListBuilder.addColumn(columnName, columnType(columnTypeToken));

                deriveColumnConstraintDefinitions(columnName, tableElementListBuilder, lexer);
        }
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
