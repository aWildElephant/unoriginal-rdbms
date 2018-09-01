package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.util.HashSet;
import java.util.Set;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.*;
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
            case FOREIGN:
                lexer.consumeNextToken();

                consumeAndExpect(KEY, lexer);
                consumeAndExpect(LEFT_PAREN, lexer);

                final Set<String> columnNames = new HashSet<>();

                columnNames.add(consumeIdentifier(lexer));

                while (lexer.lookupNextToken().type() == COMMA) {
                    lexer.consumeNextToken();
                    columnNames.add(consumeIdentifier(lexer));
                }

                consumeAndExpect(RIGHT_PAREN, lexer);
                consumeAndExpect(REFERENCES, lexer);

                final String targetTableName = consumeIdentifier(lexer);

                consumeAndExpect(LEFT_PAREN, lexer);

                final Set<String> targetColumnNames = new HashSet<>();

                targetColumnNames.add(consumeIdentifier(lexer));

                while (lexer.lookupNextToken().type() == COMMA) {
                    lexer.consumeNextToken();
                    targetColumnNames.add(consumeIdentifier(lexer));
                }

                consumeAndExpect(RIGHT_PAREN, lexer);

                tableElementListBuilder.addForeignKeyConstraint(columnNames, targetTableName, targetColumnNames);

                break;
            case UNIQUE:
                lexer.consumeNextToken();

                consumeAndExpect(LEFT_PAREN, lexer);

                final HashSet<String> columns = new HashSet<>();

                columns.add(consumeIdentifier(lexer));

                while (lexer.lookupNextToken().type() == COMMA) {
                    lexer.consumeNextToken();

                    columns.add(consumeIdentifier(lexer));
                }

                consumeAndExpect(RIGHT_PAREN, lexer);

                tableElementListBuilder.addUniqueConstraint(columns);

                break;
            default:
                final String columnName = consumeIdentifier(lexer);
                final Token columnTypeToken = lexer.consumeNextToken();
                final int columnType = columnType(columnTypeToken, lexer);

                tableElementListBuilder.addColumn(columnName, columnType);

                deriveColumnConstraintDefinitions(columnName, tableElementListBuilder, lexer);
        }
    }

    private static int columnType(Token columnTypeToken, Lexer lexer) {
        switch (columnTypeToken.type()) {
            case CHAR:
                ignoreLengthInformation(lexer);

                return ColumnDefinition.TEXT;
            case DECIMAL:
                return ColumnDefinition.DECIMAL;
            case INTEGER:
                return ColumnDefinition.INTEGER;
            case TEXT:
                return ColumnDefinition.TEXT;
            case VARCHAR:
                ignoreLengthInformation(lexer);

                return ColumnDefinition.TEXT;
            default:
                throw unexpectedToken(columnTypeToken);
        }
    }

    private static void ignoreLengthInformation(Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);
        consumeAndExpect(INTEGER_LITERAL, lexer);
        consumeAndExpect(RIGHT_PAREN, lexer);
    }
}
