package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ColumnConstraintDefinitionsRule.deriveColumnConstraintDefinitions;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableElementRule {

    private TableElementRule() {

    }

    static ColumnDefinition deriveTableElement(final Lexer lexer) {
        final String columnName = consumeIdentifier(lexer);

        final Token columnTypeToken = lexer.consumeNextToken();

        final ColumnDefinition.Builder builder = ColumnDefinition.builder(columnName, columnType(columnTypeToken));

        deriveColumnConstraintDefinitions(builder, lexer);

        return builder.build();
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
