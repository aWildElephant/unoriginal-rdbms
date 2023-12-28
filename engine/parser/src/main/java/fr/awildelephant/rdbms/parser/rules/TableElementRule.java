package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.ColumnType;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.util.HashSet;
import java.util.Set;

import static fr.awildelephant.rdbms.ast.ColumnDefinition.column;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.COMMA;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTEGER_LITERAL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.KEY;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.REFERENCES;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.error.ErrorHelper.unexpectedToken;
import static fr.awildelephant.rdbms.parser.rules.ColumnConstraintDefinitionsRule.deriveColumnConstraintDefinitions;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIfNextTokenIs;

final class TableElementRule {

    private TableElementRule() {

    }

    static void deriveTableElement(final TableElementList.Builder tableElementListBuilder, final Lexer lexer) {
        final Token token = lexer.lookupNextToken();

        switch (token.type()) {
            case FOREIGN -> {
                lexer.consumeNextToken();
                consumeAndExpect(KEY, lexer);
                consumeAndExpect(LEFT_PAREN, lexer);
                final Set<String> columnNames = new HashSet<>();
                columnNames.add(consumeIdentifier(lexer));
                while (consumeIfNextTokenIs(COMMA, lexer)) {
                    columnNames.add(consumeIdentifier(lexer));
                }
                consumeAndExpect(RIGHT_PAREN, lexer);
                consumeAndExpect(REFERENCES, lexer);
                final String targetTableName = consumeIdentifier(lexer);
                consumeAndExpect(LEFT_PAREN, lexer);
                final Set<String> targetColumnNames = new HashSet<>();
                targetColumnNames.add(consumeIdentifier(lexer));
                while (consumeIfNextTokenIs(COMMA, lexer)) {
                    targetColumnNames.add(consumeIdentifier(lexer));
                }
                consumeAndExpect(RIGHT_PAREN, lexer);
                tableElementListBuilder.addForeignKeyConstraint(columnNames, targetTableName, targetColumnNames);
            }
            case UNIQUE -> {
                lexer.consumeNextToken();
                consumeAndExpect(LEFT_PAREN, lexer);
                final HashSet<String> columns = new HashSet<>();
                columns.add(consumeIdentifier(lexer));
                while (consumeIfNextTokenIs(COMMA, lexer)) {
                    columns.add(consumeIdentifier(lexer));
                }
                consumeAndExpect(RIGHT_PAREN, lexer);
                tableElementListBuilder.addUniqueConstraint(columns);
            }
            default -> {
                final ColumnDefinition column = deriveColumnDefinition(lexer);
                tableElementListBuilder.addColumn(column);
                deriveColumnConstraintDefinitions(column.columnName(), tableElementListBuilder, lexer);
            }
        }
    }

    public static ColumnDefinition deriveColumnDefinition(final Lexer lexer) {
        final String columnName = consumeIdentifier(lexer);
        final ColumnType columnType = deriveColumnType(lexer);
        return column(columnName, columnType);
    }

    private static ColumnType deriveColumnType(final Lexer lexer) {
        final Token token = lexer.consumeNextToken();
        return columnType(token, lexer);
    }

    private static ColumnType columnType(final Token columnTypeToken, final Lexer lexer) {
        switch (columnTypeToken.type()) {
            case BOOLEAN -> {
                return ColumnType.BOOLEAN;
            }
            case BIGINT -> {
                return ColumnType.BIGINT;
            }
            case DATE -> {
                return ColumnType.DATE;
            }
            case DECIMAL -> {
                return ColumnType.DECIMAL;
            }
            case INTEGER -> {
                return ColumnType.INTEGER;
            }
            case TEXT -> {
                return ColumnType.TEXT;
            }
            case CHAR, VARCHAR -> {
                ignoreLengthInformation(lexer);
                return ColumnType.TEXT;
            }
            default -> throw unexpectedToken(columnTypeToken);
        }
    }

    private static void ignoreLengthInformation(final Lexer lexer) {
        consumeAndExpect(LEFT_PAREN, lexer);
        consumeAndExpect(INTEGER_LITERAL, lexer);
        consumeAndExpect(RIGHT_PAREN, lexer);
    }
}
