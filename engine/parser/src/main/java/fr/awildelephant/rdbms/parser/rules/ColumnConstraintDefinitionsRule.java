package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import java.util.Set;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.LEFT_PAREN;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.RIGHT_PAREN;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class ColumnConstraintDefinitionsRule {

    private ColumnConstraintDefinitionsRule() {

    }

    static void deriveColumnConstraintDefinitions(String columnName, TableElementList.Builder tableElementListBuilder,
                                                  Lexer lexer) {
        while (true) {
            final Token token = lexer.lookupNextToken();
            switch (token.type()) {
                case NOT -> {
                    lexer.consumeNextToken();
                    consumeAndExpect(NULL, lexer);
                    tableElementListBuilder.addNotNullConstraint(columnName);
                }
                case REFERENCES -> {
                    lexer.consumeNextToken();
                    final String targetTableName = consumeIdentifier(lexer);
                    consumeAndExpect(LEFT_PAREN, lexer);
                    final String targetColumnName = consumeIdentifier(lexer);
                    consumeAndExpect(RIGHT_PAREN, lexer);
                    tableElementListBuilder
                            .addForeignKeyConstraint(Set.of(columnName), targetTableName, Set.of(targetColumnName));
                }
                case UNIQUE -> {
                    lexer.consumeNextToken();
                    tableElementListBuilder.addUniqueConstraint(columnName);
                }
                default -> {
                    return;
                }
            }
        }
    }
}
