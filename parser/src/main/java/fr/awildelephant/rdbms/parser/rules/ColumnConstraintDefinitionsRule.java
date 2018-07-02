package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.lexer.tokens.Token;

import static fr.awildelephant.rdbms.lexer.tokens.TokenType.NULL;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;

final class ColumnConstraintDefinitionsRule {

    private ColumnConstraintDefinitionsRule() {

    }

    static void deriveColumnConstraintDefinitions(String columnName, TableElementList.Builder tableElementListBuilder,
                                                  Lexer lexer) {
        while (true) {
            final Token token = lexer.lookupNextToken();
            switch (token.type()) {
                case NOT:
                    lexer.consumeNextToken();
                    consumeAndExpect(NULL, lexer);

                    tableElementListBuilder.addNotNullConstraint(columnName);
                    break;
                case UNIQUE:
                    lexer.consumeNextToken();

                    tableElementListBuilder.addUniqueConstraint(columnName);
                    break;
                default:
                    return;
            }
        }
    }
}
